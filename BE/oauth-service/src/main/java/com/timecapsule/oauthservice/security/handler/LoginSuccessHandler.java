package com.timecapsule.oauthservice.security.handler;

import com.timecapsule.oauthservice.api.response.TokenResponse;
import com.timecapsule.oauthservice.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
// 로그인 성공시 처리할 로직
// CustomOAuth2Service 이후로 실행되는 핸들러 메서드
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper; // Java 객체를 JSON으로 serialization(response에 정보를 담기 위해서 사용)

    @Override
    // 로그인 한 사용자를 위해 Access Token, Refresh Token 발급
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("로그인 성공 후 토큰 발급을 시작합니다.");

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String refreshToken = tokenProvider.createRefreshToken(authentication);
        String accessToken = tokenProvider.createAccessToken(authentication);
        TokenResponse tokenResponse = TokenResponse.of(accessToken,refreshToken);

        objectMapper.writeValue(response.getWriter(), tokenResponse);
    }
}