package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.MessageReq;
import com.timecapsule.chatservice.api.response.ChatMessageRes;
import com.timecapsule.chatservice.db.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage sendMessage(MessageReq message);
    List<ChatMessageRes> getMessageByRoomId(String roomId);
}
