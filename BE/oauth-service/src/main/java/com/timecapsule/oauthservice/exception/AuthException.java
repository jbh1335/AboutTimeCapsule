package com.timecapsule.oauthservice.exception;

import org.springframework.http.HttpStatus;

public abstract class AuthException extends ApplicationException {

    protected AuthException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}