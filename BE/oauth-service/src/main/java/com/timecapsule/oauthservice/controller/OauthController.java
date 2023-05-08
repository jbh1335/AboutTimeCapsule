package com.timecapsule.oauthservice.controller;

import com.timecapsule.oauthservice.api.request.AuthorizationRequest;
import com.timecapsule.oauthservice.api.request.RefreshTokenRequest;
import com.timecapsule.oauthservice.api.response.AccessTokenResponse;
import com.timecapsule.oauthservice.api.response.CustomResponse;
import com.timecapsule.oauthservice.api.response.LoginResponse;
import com.timecapsule.oauthservice.security.jwt.AuthorizationExtractor;
import com.timecapsule.oauthservice.service.AuthService;
import com.timecapsule.oauthservice.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Slf4j
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final AuthService authService;


    // 인증 코드로 로그인
    @GetMapping("/login/oauth/{provider}")
    public LoginResponse login(@PathVariable String provider, @RequestParam String code) {
        log.info("회원가입/로그인 시도");
        return oauthService.login(new AuthorizationRequest(provider, code));

    }

    // Refresh Token으로 Access Token을 갱신
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AccessTokenResponse updateAccessToken(HttpServletRequest request, @Validated RefreshTokenRequest refreshToken) {
        String accessToken = AuthorizationExtractor.extract(request);
        log.info("accessToken = {}", accessToken);
        return authService.accessTokenByRefreshToken(accessToken, refreshToken);
    }

    // Access Token으로 로그아웃
    @PostMapping("/logout")
    public CustomResponse logout(HttpServletRequest request) {
        String accessToken = AuthorizationExtractor.extract(request);
        return authService.logout(accessToken);
    }
}