package com.timecapsule.oauthservice.security;

import com.timecapsule.oauthservice.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendErrorUtil {
    /*
    * 401 : Unauthorized, 인증 실패, 로그인하지 않은 사용자 또는 권한 없는 사용자 처리
    * 403 : Forbidden, 인증 성공 그러나 자원에 대한 권한 없음. 삭제, 수정시 권한 없음
    * 500 : 서버 에러
    * */
    private static final String UNAUTHORIZED_MESSAGE = "유효하지 않은 토큰";
    private static final String UNAUTHORIZED_CODE = "401";
    private static final String SERVER_ERROR_MESSAGE = "서버 에러";
    private static final String SERVER_ERROR_CODE = "500";

    public static void sendUnauthorizedErrorResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(
                ErrorResponse.of(UNAUTHORIZED_MESSAGE, UNAUTHORIZED_CODE));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writeErrorResponse(response, errorResponse);
    }

    public static void sendServerErrorResponse(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
        String errorResponse = objectMapper.writeValueAsString(
                ErrorResponse.of(SERVER_ERROR_MESSAGE, SERVER_ERROR_CODE));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        writeErrorResponse(response, errorResponse);
    }

    private static void writeErrorResponse(HttpServletResponse response, String errorResponse) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(errorResponse);
    }
}
