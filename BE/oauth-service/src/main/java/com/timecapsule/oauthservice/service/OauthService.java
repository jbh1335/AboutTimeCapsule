package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.request.AuthorizationRequest;
import com.timecapsule.oauthservice.api.response.LoginResponse;
import com.timecapsule.oauthservice.api.response.OauthTokenResponse;
import com.timecapsule.oauthservice.config.redis.RedisUtil;
import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.entity.RoleType;
import com.timecapsule.oauthservice.db.repository.MemberRepository;
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

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OauthService {
    private static final String BEARER_TYPE = "Bearer";
    private final InMemoryClientRegistrationRepository inMemoryRepository; // application-oauth 정보가 담김
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public LoginResponse login(AuthorizationRequest authorizationRequest) {
        // provider 이름을 통해 InMemoryProviderRepository에서 OauthProvider 가져오기
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(authorizationRequest.getProviderName());
        log.info("{} 로그인 요청", provider.getClientName());
        log.info("provider : {}", provider);

        Member member = getUserInfo(authorizationRequest, provider);

        log.info("Member = {}", member);

        // 유저 인증 후 Jwt AccessToken, Refresh Token 생성
        // 추후에 DB에 있는 정보와 비교하기 위해서는 해당 사용자만이 가질 수 있는 고유한 값이 필요하기 때문에, 토큰 생성은 memberId를 포함하여 구성
        Token accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
        log.info("accessToken = {}", accessToken.getValue());
        Token refreshToken = jwtTokenProvider.createRefreshToken();
        log.info("refreshToken = {}", refreshToken.getValue());

        redisUtil.setDataExpire(String.valueOf(member.getId()), refreshToken.getValue(), refreshToken.getExpiredTime());
        
        log.info("로그인 완료");
        return LoginResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImageUrl())
                .roleType(member.getRoleType())
                .tokenType(BEARER_TYPE)
                .accessToken(accessToken.getValue())
                .refreshToken(refreshToken.getValue())
                .build();
    }

    private Member getUserInfo(AuthorizationRequest authorizationRequest, ClientRegistration provider) {
        OauthTokenResponse token = getToken(authorizationRequest, provider);
        if(token.getAccessToken() == null)
            log.info("AccessToken 발급 실패");
        else
            log.info("AccessToken 발급 성공 : {}", token);
        Map<String, Object> userAttributes = getUserAttributes(provider, token);
        Member extract = OauthAttributes.extract(authorizationRequest.getProviderName(), userAttributes);
        return saveOrUpdate(extract);
    }

    private Member saveOrUpdate(Member member) {
        Member findMember = memberRepository.findByProviderId(member.getProviderId()).orElse(null);
        if (findMember == null) {
            findMember = memberRepository.save(member);
            log.info("회원가입 진행 - 회원번호 {}", findMember.getId());
        }
        else {
            findMember.updateProfileImageUrl(member.getProfileImageUrl());
            log.info("로그인 진행 - 회원번호 {}", findMember.getId());
        }
        return findMember;
    }

    private OauthTokenResponse getToken(AuthorizationRequest authorizationRequest, ClientRegistration provider) {
//        log.info("Authorization Code로 Oauth 서버에 Token 요청");
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(authorizationRequest, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    // 토큰을 받기 위한 HTTP Body 생성
    private MultiValueMap<String, String> tokenRequest(AuthorizationRequest authorizationRequest, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", authorizationRequest.getCode());
        formData.add("grant_type", "authorization_code");
        formData.add("client_secret",provider.getClientSecret());
        formData.add("client_id",provider.getClientId());
        formData.add("state", new BigInteger(130, new SecureRandom()).toString());
        return formData;
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}