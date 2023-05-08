package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.api.request.MessageReq;
import com.timecapsule.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    //TODO : ChatMessage 객체가 아니라 Message 객체를 받도록 하기
    public void message(MessageReq message) {
        chatMessageService.sendMessage(message);
    }
}
