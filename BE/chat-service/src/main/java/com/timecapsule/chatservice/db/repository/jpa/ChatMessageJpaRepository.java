package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.db.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
}
