package com.timecapsule.oauthservice.exception;

import lombok.Getter;

@Getter
// 사용자 정의 예외
// 컴파일러가 체크하지 않는 실행 예외로 선언(RuntimeException 상속)
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}