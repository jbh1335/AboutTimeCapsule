package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.api.request.MessageReq;
import com.timecapsule.chatservice.service.RedisPubSubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("메시지 API")
@RequiredArgsConstructor
@RestController
public class RedisPubSubController {
    private final RedisPubSubService redisPubSubService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @ApiOperation(value = "메시지 보내기", notes = "메시지를 보낸다")
    @MessageMapping("/chat/message")
    public ResponseEntity<?> message(@RequestBody MessageReq message) {
        return new ResponseEntity<>(redisPubSubService.sendMessage(message), HttpStatus.CREATED);
    }
}
