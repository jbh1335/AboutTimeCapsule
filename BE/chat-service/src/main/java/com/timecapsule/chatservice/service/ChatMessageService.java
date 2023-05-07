package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.db.entity.ChatMessage;

public interface ChatMessageService {
    void sendMessage(ChatMessage message);
}
