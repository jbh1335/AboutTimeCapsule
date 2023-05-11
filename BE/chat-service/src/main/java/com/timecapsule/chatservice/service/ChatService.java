package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.api.response.ChatMessageRes;
import com.timecapsule.chatservice.api.response.ChatroomRes;
import com.timecapsule.chatservice.db.entity.Chatroom;

import java.util.List;

public interface ChatService {
    Chatroom createChatroom(ChatroomReq chatroomReq);
    List<ChatroomRes> getMyChatroomList(Integer memberId);

    List<ChatMessageRes> getMessageByRoomId(String roomId);
}
