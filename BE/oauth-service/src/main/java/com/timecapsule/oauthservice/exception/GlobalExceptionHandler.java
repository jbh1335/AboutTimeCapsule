package com.timecapsule.oauthservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
// (Rest)ControllerAdvice는 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용
// 전역적으로 에러를 핸들링하는 클래스를 만들어 어노테이션을 붙여줌으로써 에러 처리를 위임
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e);
    }
}