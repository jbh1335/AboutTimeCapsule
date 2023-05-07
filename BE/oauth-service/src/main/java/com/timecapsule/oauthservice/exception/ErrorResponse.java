package com.timecapsule.oauthservice.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final int status; // 결과 코드
    private final String error; 
    private final String code; // enum으로 정의한 에러
    private final String errorMessage; // 에러 메시지
    private final String detailMessage; // 에러 상세 메시지


    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .errorMessage(e.getMessage())
                        .detailMessage(errorCode.getDetailMessage())
                        .build());
    }
}
