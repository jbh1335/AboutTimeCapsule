package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.api.response.ChatroomRes;

import java.util.List;

public interface ChatroomRepositoryCustom {
    List<ChatroomRes> findChatroomListByMemberId(Integer memberId);
}
