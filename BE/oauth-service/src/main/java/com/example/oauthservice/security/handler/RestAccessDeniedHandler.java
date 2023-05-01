package com.example.oauthservice.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
// AccessDeniedHandler는 서버에 요청을 할 때 액세스가 가능한지 권한을 체크후 액세스 할 수 없는 요청을 했을시 동작
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private static final String FORBIDDEN_MESSAGE = "권한 없음";

    @Override
    // 인가 처리 과정에서 예외가 발생한 경우 handle 메소드를 실행됨
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN_MESSAGE);
    }
}