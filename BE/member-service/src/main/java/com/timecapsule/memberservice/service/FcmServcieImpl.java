package com.timecapsule.memberservice.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.timecapsule.memberservice.db.entity.Alarm;
import com.timecapsule.memberservice.db.entity.Friend;
import com.timecapsule.memberservice.db.entity.Member;
import com.timecapsule.memberservice.db.repository.AlarmRepository;
import com.timecapsule.memberservice.db.repository.MemberRepository;
import com.timecapsule.memberservice.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("fcmService")
@RequiredArgsConstructor
public class FcmServcieImpl implements FcmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Override
    public void sendMessage(MessageDto messageDto) {
        Message message = Message.builder()
                .setToken(messageDto.getTargetToken())
                .setNotification(Notification.builder()
                        .setTitle(messageDto.getTitle())
                        .setBody(messageDto.getBody())
                        .build())
                .putAllData(messageDto.getDataMap())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);

            Optional<Member> oMember = memberRepository.findById(Integer.parseInt(messageDto.getDataMap().get("memberId")));
            Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

            alarmRepository.save(Alarm.builder()
                    .member(member)
                    .content(messageDto.getBody())
                    .categoryType(messageDto.getCategoryType())
                    .capsuleId(Integer.parseInt(messageDto.getDataMap().get("capsuleId")))
                    .build());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void friendRequestNotification(Friend friend, Member sender, Member receiver) {
        
    }

    @Override
    public void acceptRequestNotification(Friend friend, Member requester, Member me) {

    }
}
