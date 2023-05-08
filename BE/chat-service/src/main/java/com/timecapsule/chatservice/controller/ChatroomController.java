package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.service.ChatroomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("채팅방 API")
@RequiredArgsConstructor
@RestController("/api/chat")
public class ChatroomController {
    private final ChatroomService chatroomService;

    @ApiOperation(value = "채팅방 생성하기", notes = "채팅방을 등록한다")
    @PostMapping("/room")
    public ResponseEntity<?> createChatroom(@RequestBody ChatroomReq chatroomReq) {
        return new ResponseEntity<>(chatroomService.createChatroom(chatroomReq), HttpStatus.CREATED);
    }
}
