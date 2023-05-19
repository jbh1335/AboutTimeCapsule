package com.timecapsule.capsuleservice.exception;

import com.timecapsule.capsuleservice.api.response.CommentRes;
import com.timecapsule.capsuleservice.api.response.CommonRes;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorRes extends CommonRes {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public ErrorRes(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public static ResponseEntity<ErrorRes> error(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorRes.builder()
                        .httpStatus(errorCode.getHttpStatus())
                        .code(errorCode.name())
                        .message(errorCode.getMessage())
                        .build());
    }
}
