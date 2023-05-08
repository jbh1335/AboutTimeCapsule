package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.MessageReq;

public interface ChatMessageService {
    void sendMessage(MessageReq message);
}
