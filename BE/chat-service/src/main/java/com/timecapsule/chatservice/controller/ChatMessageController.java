package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.db.entity.ChatMessage;
import com.timecapsule.chatservice.db.repository.redis.ChatroomRedisRepository;
import com.timecapsule.chatservice.service.pubsub.ChatMessageService;
import com.timecapsule.chatservice.service.pubsub.RedisPublisher;
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
    public void message(ChatMessage message) {
        chatMessageService.sendMessage(message);
    }
}
