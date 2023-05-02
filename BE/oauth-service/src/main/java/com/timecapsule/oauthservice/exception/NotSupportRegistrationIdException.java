package com.timecapsule.oauthservice.exception;

import org.springframework.http.HttpStatus;

public class NotSupportRegistrationIdException extends AuthException{
    private static final String MESSAGE = "미지원 소셜 로그인";
    private static final String CODE = "400";

    public NotSupportRegistrationIdException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}