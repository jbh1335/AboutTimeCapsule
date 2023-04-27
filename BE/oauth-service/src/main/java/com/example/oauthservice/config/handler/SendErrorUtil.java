package com.example.oauthservice.config.handler;

import com.example.oauthservice.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendErrorUtil {
    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String INVALID_TOKEN_CODE = "401";
    private static final String LOGIN_FAIL_MESSAGE = "로그인에 실패했습니다.";
    private static final String LOGIN_FAIL_CODE = "401";
    private static final String SERVER_ERROR_MESSAGE = "서버에 문제가 발생했습니다.";
    private static final String SERVER_ERROR_CODE = "500";
    private static final String FORBIDDEN_MESSAGE = "권한이 없습니다.";
    private static final String FORBIDDEN_CODE = "403";


    public static void sendInvalidTokenErrorResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(
                ErrorResponse.of(INVALID_TOKEN_MESSAGE, INVALID_TOKEN_CODE));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writeErrorResponse(response, errorResponse);
    }

    private static void writeErrorResponse(HttpServletResponse response, String errorResponse) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(errorResponse);
    }
}
