package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.db.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, String>, ChatroomRepositoryCustom {
    Chatroom save(Chatroom chatroom);
    Optional<Chatroom> findById(String roomId);
}
