package com.timecapsule.chatservice.db.repository.redis;

import com.sun.xml.bind.v2.TODO;
import com.timecapsule.chatservice.db.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatMessageRedisRepository {
    private final String CHAT_MESSAGES = "CHAT_MESSAGES";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatMessage> opsHashChatMessage;
    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessageList;

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
        opsHashChatMessageList = redisTemplate.opsForHash();
    }

    public void createMessage(String roomId, ChatMessage chatMessage) {
        List<ChatMessage> chatMessageList = opsHashChatMessageList.get(CHAT_MESSAGES, roomId);

        if(chatMessageList == null){
            chatMessageList = new ArrayList<>();
        }

        chatMessage.setId(chatMessageList.size() + 1);
        chatMessageList.add(chatMessage);
        opsHashChatMessageList.put(CHAT_MESSAGES, roomId, chatMessageList);
    }
}
