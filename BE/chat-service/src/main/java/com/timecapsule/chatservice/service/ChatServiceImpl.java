package com.timecapsule.chatservice.service;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.api.response.ChatMessageRes;
import com.timecapsule.chatservice.api.response.ChatroomRes;
import com.timecapsule.chatservice.db.entity.ChatMessage;
import com.timecapsule.chatservice.db.entity.Chatroom;
import com.timecapsule.chatservice.db.entity.Member;
import com.timecapsule.chatservice.db.repository.jpa.ChatMessageJpaRepository;
import com.timecapsule.chatservice.db.repository.jpa.ChatroomJpaRepository;
import com.timecapsule.chatservice.db.repository.jpa.MemberRepository;
import com.timecapsule.chatservice.db.repository.redis.ChatroomRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatroomRedisRepository chatroomRedisRepository;
    private final MemberRepository memberRepository;
    private final ChatroomJpaRepository chatroomJpaRepository;

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public Chatroom createChatroom(ChatroomReq chatroomReq) {
        //chatroom 객체 생성
        Member fromMember = memberRepository.findById(chatroomReq.getFromMemberId())
                .orElseThrow(NullPointerException::new);
        Member toMember = memberRepository.findById(chatroomReq.getToMemberId())
                .orElseThrow(NullPointerException::new);

        Chatroom chatroom = Chatroom.builder()
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
//        return chatroomJpaRepository.findChatroomListByMemberId(memberId);
        return null;
    }

    @Override
    public List<ChatMessageRes> getMessageByRoomId(String roomId) {
        Chatroom chatroom = chatroomJpaRepository.findById(roomId).orElseThrow(NullPointerException::new);
        List<ChatMessage> chatMessageList = chatMessageJpaRepository.findByChatroom(chatroom);

        List<ChatMessageRes> messageResList = new ArrayList<>();

        if(chatMessageList != null) {
            for (ChatMessage chatMessage : chatMessageList) {
                System.out.println(chatMessage);

                ChatMessageRes chatMessageRes = ChatMessageRes.builder()
                        .nickname(chatMessage.getSender().getNickname())
                        .profileImageUrl(chatMessage.getSender().getProfileImageUrl())
                        .content(chatMessage.getContent())
                        .createdDate(chatMessage.getCreatedDate())
                        .build();

                System.out.println(chatMessageRes);

                messageResList.add(chatMessageRes);
            }
        }
        return messageResList;
    }

}
