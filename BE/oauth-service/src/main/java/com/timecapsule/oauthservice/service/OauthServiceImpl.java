package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.LoginRes;
import com.timecapsule.oauthservice.api.response.OauthTokenRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
import com.timecapsule.oauthservice.config.redis.RedisUtil;
import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.repository.MemberRepository;
import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.exception.ErrorCode;
import com.timecapsule.oauthservice.security.OauthAttributes;
import com.timecapsule.oauthservice.security.jwt.JwtTokenProvider;
import com.timecapsule.oauthservice.security.jwt.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OauthServiceImpl implements OauthService{
    private static final String BEARER_TYPE = "Bearer";
    private final InMemoryClientRegistrationRepository inMemoryRepository; // application-oauth 정보가 담김
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public SuccessRes<LoginRes> login(String providerName, String token) {
        if (token == null) throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
        // provider 이름을 통해 InMemoryProviderRepository에서 OauthProvider 가져오기
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        log.info("{} 로그인 요청", provider.getClientName());
        log.info("provider : {}", provider);

        Member member = getUserInfo(token, provider);

        log.info("Member = {}", member);

        // 유저 인증 후 Jwt AccessToken, Refresh Token 생성
        // 추후에 DB에 있는 정보와 비교하기 위해서는 해당 사용자만이 가질 수 있는 고유한 값이 필요하기 때문에, 토큰 생성은 memberId를 포함하여 구성
        Token accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
        log.info("accessToken = {}", accessToken.getValue());
        Token refreshToken = jwtTokenProvider.createRefreshToken();
        log.info("refreshToken = {}", refreshToken.getValue());

        redisUtil.setDataExpire(String.valueOf(member.getId()), refreshToken.getValue(), refreshToken.getExpiredTime());
        
        log.info("로그인 완료");
        LoginRes loginRes = LoginRes.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImageUrl())
                .accessToken(accessToken.getValue())
                .refreshToken(refreshToken.getValue())
                .build();
        return new SuccessRes<>(true, "로그인을 완료했습니다.", loginRes);
    }

    private Member getUserInfo(String token, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, token);
        Member extract = OauthAttributes.extract(provider.getClientName(), userAttributes);
        return saveOrUpdate(extract);
    }

    private Member saveOrUpdate(Member member) {
        Member findMember = memberRepository.findByProviderId(member.getProviderId()).orElse(null);
        if (findMember == null) {
            findMember = memberRepository.save(member);
            log.info("비회원이므로 회원가입 후 로그인 진행 - 회원번호 {}", findMember.getId());
        }
        else {
            findMember.updateProfileImageUrl(member.getProfileImageUrl());
            log.info("가입된 회원이므로 로그인 진행 - 회원번호 {}", findMember.getId());
        }
        return findMember;
    }

    public SuccessRes<OauthTokenRes> getOauthToken(String providerName, String code) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
//        log.info("Authorization Code로 Oauth 서버에 Token 요청");
        return new SuccessRes<>(true, "Oauth Token이 발급되었습니다.",
                WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenRes.class)
                .block());
    }

    // 토큰을 받기 위한 HTTP Body 생성
    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("client_secret",provider.getClientSecret());
        formData.add("client_id",provider.getClientId());
        formData.add("state", new BigInteger(130, new SecureRandom()).toString());
        return formData;
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, String token) {
        try {
            return (WebClient.create()
                    .get()
                    .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                    .headers(header -> header.setBearerAuth(token))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block());
        } catch (WebClientResponseException we){
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
        }
    }
}