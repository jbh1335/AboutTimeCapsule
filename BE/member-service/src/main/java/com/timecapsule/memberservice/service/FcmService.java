package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.db.entity.Friend;
import com.timecapsule.memberservice.db.entity.Member;
import com.timecapsule.memberservice.dto.MessageDto;

public interface FcmService {
    void sendMessage(MessageDto messageDto);
    void friendRequestNotification(Friend friend, Member sender, Member receiver);
    void acceptRequestNotification(Friend friend, Member requester, Member me);
}
