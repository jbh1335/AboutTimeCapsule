package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.AccessTokenResponse;
import com.timecapsule.oauthservice.api.response.CustomResponse;
import com.timecapsule.oauthservice.api.request.RefreshTokenRequest;
import com.timecapsule.oauthservice.config.redis.RedisUtil;
import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.exception.ErrorCode;
import com.timecapsule.oauthservice.security.jwt.JwtTokenProvider;
import com.timecapsule.oauthservice.security.jwt.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RedisUtil redisUtil;

    // Access Token 유효 검사
    public void validateAccessToken(String accessToken) {
        accessTokenExtractor(accessToken);
    }

    @Transactional(readOnly = true)
    public int findMemberByToken(String accessToken) {
        if (!accessToken.isEmpty()) {
            accessTokenExtractor(accessToken);
        }
        int id = Integer.parseInt(jwtTokenProvider.getPayload(accessToken));
        Member findUser = memberService.findById(id);
        return findUser.getId();
    }


    // Access Token 이 만료 되었을 경우 refresh Token 으로 재발급
    // Redis Server 에서 refresh Token 을 가져옴
    public AccessTokenResponse accessTokenByRefreshToken(String accessToken, RefreshTokenRequest refreshTokenRequest) {
        refreshTokenExtractor(refreshTokenRequest);
        String id = jwtTokenProvider.getPayload(accessToken);
        String data = redisUtil.getData(id);

        // 조회해온 RefreshToken과 요청으로 넘어온 RefresToken과 동일하지 않다면 이는 유효하지 않은 RefreshToken
        if (!data.equals(refreshTokenRequest.getRefreshToken())) {
            log.info("유효하지 않은 Refresh Token으로 인한 에러 발생");
            throw new CustomException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN);
        }

        Token newAccessToken = jwtTokenProvider.createAccessToken(id);
        log.info("새로운 AccessToken : {}", newAccessToken);

        return new AccessTokenResponse(newAccessToken.getValue());
    }
    
    @Transactional
    public CustomResponse logout(String accessToken) {
        String id = jwtTokenProvider.getPayload(accessToken);
        redisUtil.deleteData(id); // 로그아웃 시 토큰 삭제
        return new CustomResponse("로그아웃 성공");
    }


    // Access Token 유효 검사
    private void accessTokenExtractor(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
        }
    }

    // Refresh Token 유효 검사
    private void refreshTokenExtractor(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN);
        }
    }
}