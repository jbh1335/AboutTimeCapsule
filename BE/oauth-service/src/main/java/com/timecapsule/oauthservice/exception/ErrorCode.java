package com.timecapsule.oauthservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
// Enum 클래스를 활용해서 에러 코드 관리
public enum ErrorCode {
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인 필요"),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "권한 없는 요청"),
    UNAUTHORIZED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token"),
    UNAUTHORIZED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "없는 사용자");

    private final HttpStatus httpStatus;
    private final String detailMessage;
}