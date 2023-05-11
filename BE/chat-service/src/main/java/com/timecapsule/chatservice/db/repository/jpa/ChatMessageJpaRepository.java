package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.api.response.ChatMessageRes;
import com.timecapsule.chatservice.db.entity.ChatMessage;
import com.timecapsule.chatservice.db.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatroom(Chatroom chatroom);
}
