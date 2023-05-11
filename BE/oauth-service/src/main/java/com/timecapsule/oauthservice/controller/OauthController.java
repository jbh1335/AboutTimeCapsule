package com.timecapsule.oauthservice.controller;

import com.timecapsule.oauthservice.api.request.RefreshTokenReq;
import com.timecapsule.oauthservice.api.response.*;
import com.timecapsule.oauthservice.security.jwt.AuthorizationExtractor;
import com.timecapsule.oauthservice.service.TokenService;
import com.timecapsule.oauthservice.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Slf4j
@RestController
public class OauthController {
    private final OauthService oauthService;
    private final TokenService tokenService;

    // 인가 코드로 로그인
    @PostMapping("/login/{providerName}")
    public SuccessRes<LoginRes> login(HttpServletRequest request, @PathVariable String providerName) {
        String token = AuthorizationExtractor.extract(request);
        return oauthService.login(providerName, token);
    }

    // Refresh Token으로 Access Token을 갱신
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public SuccessRes<AccessTokenRes> updateAccessToken(HttpServletRequest request, @Validated RefreshTokenReq refreshToken) {
        String accessToken = AuthorizationExtractor.extract(request);
        log.info("accessToken = {}", accessToken);
        return tokenService.accessTokenByRefreshToken(accessToken, refreshToken);
    }

    // Access Token으로 로그아웃
    @PostMapping("/logout/me")
    public CommonRes logout(HttpServletRequest request) {
        String accessToken = AuthorizationExtractor.extract(request);
        return tokenService.logout(accessToken);
    }

    @GetMapping("/{providerName}/callback")
    public SuccessRes<OauthTokenRes> getOauthToken(@PathVariable String providerName, @RequestParam String code) {
        return oauthService.getOauthToken(providerName, code);
    }
}