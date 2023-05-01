package com.timecapsule.oauthservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException{
    private static final String MESSAGE = "존재하지 않는 회원";
    private static final String CODE = "400";

    public UserNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}