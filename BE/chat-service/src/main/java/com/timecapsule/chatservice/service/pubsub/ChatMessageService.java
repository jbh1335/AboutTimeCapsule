package com.timecapsule.chatservice.service.pubsub;

import com.timecapsule.chatservice.db.entity.ChatMessage;

public interface ChatMessageService {
    void sendMessage(ChatMessage message);
}
