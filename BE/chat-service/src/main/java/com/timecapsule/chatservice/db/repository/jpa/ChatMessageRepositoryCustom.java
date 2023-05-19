package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.api.response.ChatMessageRes;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    List<ChatMessageRes> viewMessagesByRoomId(String roomId);
}
