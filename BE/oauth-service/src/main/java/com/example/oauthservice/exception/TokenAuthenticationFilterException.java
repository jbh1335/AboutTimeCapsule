package com.example.oauthservice.exception;


import org.springframework.http.HttpStatus;


public class TokenAuthenticationFilterException extends AuthException {

    private static final String MESSAGE = "유효하지 않은 토큰";
    private static final String CODE = "401";

    public TokenAuthenticationFilterException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}