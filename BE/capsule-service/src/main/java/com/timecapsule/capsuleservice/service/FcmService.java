package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.db.entity.Capsule;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.entity.Memory;
import com.timecapsule.capsuleservice.dto.MessageDto;

public interface FcmService {
    void sendMessage(MessageDto messageDto);
    void commentNotification(Memory memory, Member sender);
    void openDateNotification(Memory memory, Member member);
    void groupCapsuleInviteNotification(Capsule capsule, Member me, Member sender);
}
