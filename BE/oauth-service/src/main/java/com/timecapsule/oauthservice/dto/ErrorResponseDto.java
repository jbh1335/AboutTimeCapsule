package com.timecapsule.oauthservice.dto;

import com.timecapsule.oauthservice.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponseDto {
    private int status;
    private String code;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.code = errorCode.name();
        this.message = errorCode.getDetailMessage();
    }

    public static ErrorResponseDto of(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }
}
