package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.request.RefreshTokenReq;
import com.timecapsule.oauthservice.api.response.AccessTokenRes;
import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
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
public class TokenServiceImpl implements TokenService{
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RedisUtil redisUtil;

    @Transactional(readOnly = true)
    public int findMemberByToken(String accessToken) {
        if (!accessToken.isEmpty()) {
            accessTokenExtractor(accessToken);
        }
        int id = Integer.parseInt(jwtTokenProvider.getPayload(accessToken));
        Member findUser = memberService.findById(id);
        return findUser.getId();
    }

    // Access Token 이 만료 되었을 경우 Refresh Token 으로 재발급
    // Redis Server 에서 Refresh Token 을 가져옴
    public SuccessRes<AccessTokenRes> accessTokenByRefreshToken(String accessToken, RefreshTokenReq refreshTokenReq) {
        refreshTokenExtractor(refreshTokenReq);
        String id = jwtTokenProvider.getPayload(accessToken);
        String data = redisUtil.getData(id);

        // 조회해온 RefreshToken과 요청으로 넘어온 RefresToken과 동일하지 않다면 이는 유효하지 않은 RefreshToken
        if (!data.equals(refreshTokenReq.getRefreshToken())) {
            log.info("유효하지 않은 Refresh Token으로 인한 에러 발생");
            throw new CustomException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN);
        }

        Token newAccessToken = jwtTokenProvider.createAccessToken(id);
        log.info("새로운 AccessToken : {}", newAccessToken);
        return new SuccessRes<>(true, "Refresh Token으로 Access Token 재발급을 완료했습니다.", new AccessTokenRes(newAccessToken.getValue()));
    }
    
    @Transactional
    public CommonRes logout(String accessToken) {
        String id = jwtTokenProvider.getPayload(accessToken);
        redisUtil.deleteData(id); // 로그아웃 시 토큰 삭제 =>  로그아웃을 하면 Refresh Token을 삭제하여 사용이 불가능하도록 함
        return new CommonRes(true, "로그아웃을 완료했습니다.");
    }

    // Access Token 유효 검사
    private void accessTokenExtractor(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
        }
    }

    // Refresh Token 유효 검사
    private void refreshTokenExtractor(RefreshTokenReq refreshTokenReq) {
        if (!jwtTokenProvider.validateToken(refreshTokenReq.getRefreshToken())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN);
        }
    }
}