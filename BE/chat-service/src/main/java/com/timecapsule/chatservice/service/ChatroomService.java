package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.db.entity.Chatroom;

public interface ChatroomService {
    Chatroom createChatroom(ChatroomReq chatroomReq);
}
