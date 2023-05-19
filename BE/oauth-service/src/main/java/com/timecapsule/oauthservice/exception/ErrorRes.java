package com.timecapsule.oauthservice.exception;

import com.timecapsule.oauthservice.api.response.CommonRes;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorRes extends CommonRes {
    private final int status; // 결과 코드
    private final String error; 
    private final String code; // enum으로 정의한 에러
    private final String detailMessage; // 에러 상세 메시지

    public ErrorRes (CustomException e) {
        super(false, e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.code = errorCode.name();
        this.detailMessage = errorCode.getDetailMessage();
    }

    public static ResponseEntity<ErrorRes> toResponseEntity(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorRes(e));
    }
}
