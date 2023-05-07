package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/chat")
public class ChatroomController {
    private final ChatroomService chatroomService;

    @PostMapping("/room")
    public ResponseEntity<?> createChatroom(@RequestBody ChatroomReq chatroomReq) {
        return new ResponseEntity<>(chatroomService.createChatroom(chatroomReq), HttpStatus.CREATED);
    }
}
