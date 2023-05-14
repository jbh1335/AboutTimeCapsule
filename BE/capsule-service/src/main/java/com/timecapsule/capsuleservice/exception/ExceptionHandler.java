//package com.timecapsule.capsuleservice.exception;
//
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class ExceptionHandler {
//    @ExceptionHandler(value = CustomException.class)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
//        // 에러에 대한 후처리를 할 수 있습니다. 본 예시에서는 로깅만 진행하였습니다.
//        log.error("[handleCustomException] {} : {}",e.getErrorCode().name(), e.getErrorCode().getMessage());
//        return ErrorResponse.error(e);
//    }
//}
