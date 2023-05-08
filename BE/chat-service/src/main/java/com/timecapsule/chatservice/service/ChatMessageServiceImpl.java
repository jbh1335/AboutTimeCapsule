package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.MessageReq;
import com.timecapsule.chatservice.db.entity.ChatMessage;
import com.timecapsule.chatservice.db.entity.Chatroom;
import com.timecapsule.chatservice.db.repository.jpa.ChatMessageJpaRepository;
import com.timecapsule.chatservice.db.repository.redis.ChatMessageRedisRepository;
import com.timecapsule.chatservice.db.repository.redis.ChatroomRedisRepository;
import com.timecapsule.chatservice.dto.MessageType;
import com.timecapsule.chatservice.service.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    //Redis 관련
    private final RedisPublisher redisPublisher;
    private final ChatroomRedisRepository chatroomRedisRepository;
    private final ChatMessageRedisRepository chatMessageRedisRepository;
    //jpa 관련
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public ChatMessage sendMessage(MessageReq message) {
        Chatroom chatroom = chatroomRedisRepository.findRoomById(message.getChatroomId());

        ChatMessage chatMessage = ChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .chatroom(chatroom)
                .content(message.getContent())
                .createdDate(LocalDateTime.now())
                .build();
        
        //채팅방 입장
        if (MessageType.ENTER.equals(message.getType())) {
            chatroomRedisRepository.enterChatroom(message.getChatroomId());
        }
        
        //Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatroomRedisRepository.getTopic(message.getChatroomId()), chatMessage);
        //redis에 저장
        chatMessageRedisRepository.createMessage(message.getChatroomId(), chatMessage);
        //jpa에 저장

        return chatMessage;
    }
}
