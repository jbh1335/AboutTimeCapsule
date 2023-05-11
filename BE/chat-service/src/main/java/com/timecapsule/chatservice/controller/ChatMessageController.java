package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.api.request.MessageReq;
import com.timecapsule.chatservice.api.response.ChatMessageRes;
import com.timecapsule.chatservice.api.response.ChatroomRes;
import com.timecapsule.chatservice.service.ChatMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("메시지 API")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @ApiOperation(value = "메시지 보내기", notes = "메시지를 보낸다")
    @MessageMapping("/chat/message")
    public ResponseEntity<?> message(@RequestBody MessageReq message) {
        return new ResponseEntity<>(chatMessageService.sendMessage(message), HttpStatus.CREATED);
    }

    @ApiOperation(value = "채팅의 메시지 보기", notes = "방번호에 해당하는 메시지 리스트를 가져온다")
    @GetMapping("/api/chat/message/{roomId}")
    public ResponseEntity<?> viewMessages(@PathVariable("roomId") String roomId) {
        try {
            List<ChatMessageRes> list = chatMessageService.getMessageByRoomId(roomId);

            if (list != null && !list.isEmpty()) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return exceptionHandling(e);
        }

    }

    private ResponseEntity<?> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
