package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.db.entity.ChatroomMessage;
import com.timecapsule.chatservice.db.repository.ChatroomRepository;
import com.timecapsule.chatservice.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatroomRepository chatroomRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatroomMessage message) {
        if (ChatroomMessage.MessageType.ENTER.equals(message.getType())) {
            chatroomRepository.enterChatRoom(message.getChatroom().getId());
            message.setContent(message.getSender() + "님이 입장하셨습니다.");
        }
        //Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatroomRepository.getTopic(message.getChatroom().getId()), message);
    }
}
