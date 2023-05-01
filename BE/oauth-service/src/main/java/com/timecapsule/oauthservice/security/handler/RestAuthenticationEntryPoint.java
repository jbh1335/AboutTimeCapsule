package com.timecapsule.oauthservice.security.handler;

import com.timecapsule.oauthservice.security.SendErrorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
// AuthenticationEntryPoint는 인증이 되지않은 유저가 요청을 했을때 동작
// 요청이 들어올시, 인증해더를 보내지 않는 경우 401(unauthoriazed) 응답 처리
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    // 인증 처리 과정에서 예외가 발생한 경우 commence 메소드를 실행됨
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        SendErrorUtil.sendUnauthorizedErrorResponse(response, objectMapper);
    }
}