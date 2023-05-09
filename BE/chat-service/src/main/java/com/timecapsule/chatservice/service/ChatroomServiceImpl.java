package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.api.response.ChatroomRes;
import com.timecapsule.chatservice.db.entity.Chatroom;
import com.timecapsule.chatservice.db.entity.Member;
import com.timecapsule.chatservice.db.repository.jpa.ChatroomJpaRepository;
import com.timecapsule.chatservice.db.repository.jpa.MemberRepository;
import com.timecapsule.chatservice.db.repository.redis.ChatroomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {
    private final ChatroomRedisRepository chatroomRedisRepository;
    private final MemberRepository memberRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;

    @Override
    public Chatroom createChatroom(ChatroomReq chatroomReq) {
        //chatroom 객체 생성
        Member fromMember = memberRepository.findById(chatroomReq.getFromMemberId())
                .orElseThrow(NullPointerException::new);
        Member toMember = memberRepository.findById(chatroomReq.getToMemberId())
                .orElseThrow(NullPointerException::new);

        Chatroom chatroom = Chatroom.builder()
                .id(UUID.randomUUID().toString())
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
        
        //Redis에 저장
        chatroomRedisRepository.createChatroom(chatroom);
        //RDB에 저장
        chatroomJpaRepository.save(chatroom);

        return chatroom;
    }

    @Override
    public List<ChatroomRes> getMyChatroomList(Integer memberId) {
        return chatroomJpaRepository.findChatroomListByMemberId(memberId);
    }
}
