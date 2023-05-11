package com.timecapsule.memberservice.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.timecapsule.memberservice.db.entity.Alarm;
import com.timecapsule.memberservice.db.entity.CategoryType;
import com.timecapsule.memberservice.db.entity.Friend;
import com.timecapsule.memberservice.db.entity.Member;
import com.timecapsule.memberservice.db.repository.AlarmRepository;
import com.timecapsule.memberservice.db.repository.MemberRepository;
import com.timecapsule.memberservice.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service("fcmService")
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {
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
                    .build());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void friendRequestNotification(Friend friend, Member me, String type) {
        String title = "";
        String body = "";

        if(type.equals("친구요청")) {
            title = "어바웃타임캡슐 - 친구 요청";
            body = friend.getFromMember().getNickname() + "님이 나에게 친구를 요청했습니다.";
        } else {
            title = "어바웃타임캡슐 - 친구 수락";
            body = friend.getToMember().getNickname() + "님이 친구 요청을 수락했습니다.";
        }

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("memberId", String.valueOf(me.getId()));
        dataMap.put("friendId", String.valueOf(friend.getId()));

        sendMessage(MessageDto.builder()
                .targetToken(me.getAlarmToken())
                .title(title)
                .body(body)
                .categoryType(CategoryType.FRIEND)
                .dataMap(dataMap)
                .build());
    }
}
