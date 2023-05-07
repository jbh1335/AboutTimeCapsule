package com.timecapsule.oauthservice.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timecapsule.oauthservice.dto.ErrorResponseDto;
import com.timecapsule.oauthservice.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
// AuthenticationEntryPoint는 인증이 되지않은 유저가 요청을 했을때 동작
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    // 인증 처리 과정에서 예외가 발생한 경우 commence 메소드를 실행됨
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("인증 처리 과정 중 예외 발생");
        Object errorObject = request.getAttribute("CustomException");
        if (errorObject != null) {
            sendError(response, ErrorCode.UNAUTHORIZED_USER);
        }
        sendError(response, ErrorCode.FORBIDDEN_USER);
    }

    private void sendError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json,charset=utf-8");
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, ErrorResponseDto.of(errorCode));
            os.flush();
        }
    }
}