package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.db.entity.ChatMessage;
import com.timecapsule.chatservice.db.repository.jpa.ChatMessageJpaRepository;
import com.timecapsule.chatservice.db.repository.redis.ChatroomRedisRepository;
import com.timecapsule.chatservice.service.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    //Redis 관련
    private final RedisPublisher redisPublisher;
    private final ChatroomRedisRepository chatroomRedisRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public void sendMessage(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatroomRedisRepository.enterChatroom(message.getChatroom().getId());
        }
        //Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatroomRedisRepository.getTopic(message.getChatroom().getId()), message);
        //redis에 저장
        chatMessageJpaRepository.save(message);
    }
}
