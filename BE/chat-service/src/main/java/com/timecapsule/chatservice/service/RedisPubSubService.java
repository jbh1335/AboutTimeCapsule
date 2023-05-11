package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.MessageReq;
import com.timecapsule.chatservice.db.entity.ChatMessage;

public interface RedisPubSubService {
    ChatMessage sendMessage(MessageReq message);

}
