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

public interface TokenService {
    int findMemberByToken(String accessToken);
    AccessTokenResponse accessTokenByRefreshToken(String accessToken, RefreshTokenRequest refreshTokenRequest);
    CustomResponse logout(String accessToken);
}