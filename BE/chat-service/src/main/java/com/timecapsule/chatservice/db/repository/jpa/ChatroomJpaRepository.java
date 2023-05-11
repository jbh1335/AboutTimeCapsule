package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.db.entity.Chatroom;
import com.timecapsule.chatservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, String>, ChatroomRepositoryCustom {
    Chatroom save(Chatroom chatroom);
    Optional<Chatroom> findById(String roomId);
    List<Chatroom> findByFromMember(Member fromMember);
    List<Chatroom> findByToMember(Member toMember);
}
