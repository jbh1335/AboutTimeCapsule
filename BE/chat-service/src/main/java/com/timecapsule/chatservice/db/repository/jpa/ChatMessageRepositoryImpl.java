package com.timecapsule.chatservice.db.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.timecapsule.chatservice.api.response.ChatMessageRes;

import javax.persistence.EntityManager;
import java.util.List;

import static com.timecapsule.chatservice.db.entity.QChatMessage.chatMessage;

public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ChatMessageRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChatMessageRes> viewMessagesByRoomId(String roomId) {

        return null;
    }
}
