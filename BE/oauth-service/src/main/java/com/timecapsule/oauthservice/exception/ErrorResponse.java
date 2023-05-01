package com.timecapsule.oauthservice.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private String code;
    private ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
    public static ErrorResponse of(String message, String code) {
        return new ErrorResponse(message, code);
    }

}
