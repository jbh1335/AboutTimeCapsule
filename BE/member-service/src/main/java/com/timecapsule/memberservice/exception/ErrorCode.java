package com.timecapsule.memberservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND ,"존재하지 않는 친구 관계입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND ,"존재하지 않는 멤버입니다."),
    OTHERMEMBER_NOT_FOUND(HttpStatus.NOT_FOUND ,"존재하지 않는 다른 멤버입니다."),
    FROMMEMBER_NOT_FOUND(HttpStatus.NOT_FOUND ,"친구 요청을 보낸 멤버가 존재하지 않습니다."),
    TOMEMBER_NOT_FOUND(HttpStatus.NOT_FOUND ,"친구 요청을 받은 멤버가 존재하지 않습니다."),
    FRIEND_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 친구 관계입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
