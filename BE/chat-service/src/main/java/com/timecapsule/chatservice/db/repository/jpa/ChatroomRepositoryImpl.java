package com.timecapsule.chatservice.db.repository.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.timecapsule.chatservice.api.response.ChatroomRes;

import javax.persistence.EntityManager;
import java.util.List;

import static com.timecapsule.chatservice.db.entity.QChatMessage.chatMessage;
import static com.timecapsule.chatservice.db.entity.QChatroom.chatroom;
import static com.timecapsule.chatservice.db.entity.QMember.member;

public class ChatroomRepositoryImpl implements ChatroomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ChatroomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

//    @Override
//    public List<ChatroomRes> findChatroomListByMemberId(Integer memberId) {
//        //TODO : error - No property 'memberId' found for type 'Chatroom'
////        List<ChatroomRes> result = queryFactory
////                .select(Projections.fields(ChatroomRes.class,
////                        new CaseBuilder()
////                                .when(chatroom.fromMember.id.eq(memberId)).then(chatroom.fromMember.nickname)
////                                .otherwise(chatroom.toMember.nickname),
////                        chatMessage.content, chatMessage.createdDate
////                        )
////                )
////                .from(chatroom.fromMember)
////                .leftJoin(chatroom.toMember)
////                .leftJoin(member)
////                .leftJoin(chatMessage.chatroom)
////                .where(chatroom.fromMember.id.eq(memberId).or(chatroom.toMember.id.eq(memberId))
////                )
////                .orderBy(chatMessage.createdDate.desc())
////                .fetch();
////
////        return result;
//
//        return null;
//    }
}
