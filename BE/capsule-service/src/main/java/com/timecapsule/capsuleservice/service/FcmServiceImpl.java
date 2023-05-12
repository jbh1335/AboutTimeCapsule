package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.*;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.AlarmRepository;
import com.timecapsule.capsuleservice.db.repository.MemberRepository;
import com.timecapsule.capsuleservice.db.repository.MemoryRepository;
import com.timecapsule.capsuleservice.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
                    .capsuleId(Integer.parseInt(messageDto.getDataMap().get("capsuleId")))
                    .build());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commentNotification(Memory memory, Member sender) {
        String title = "어바웃타임캡슐 - 댓글";
        String body = sender.getNickname() + "님이 당신의 [" + memory.getTitle() + "] 추억에 댓글을 남겼습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(memory.getCapsule().getId()));
        dataMap.put("memoryId", String.valueOf(memory.getId()));
        dataMap.put("memberId", String.valueOf(memory.getMember().getId()));

        if(memory.getMember().getAlarmToken() == null || memory.getMember().getAlarmToken().isEmpty()) return;
        sendMessage(MessageDto.builder()
                .targetToken(memory.getMember().getAlarmToken())
                .title(title)
                .body(body)
                .categoryType(CategoryType.valueOf("COMMENT"))
                .dataMap(dataMap)
                .build());
    }

    @Override
    public void openDateNotification(Memory memory, Member member) {
        String title = "어바웃타임캡슐 - 추억 오픈";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String body = dateTimeFormatter.format(memory.getOpenDate()) + "에 생성한 추억이 " +
                member.getNickname() + "님의 방문을 기다리고 있습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(memory.getCapsule().getId()));
        dataMap.put("memoryId", String.valueOf(memory.getId()));
        dataMap.put("memberId", String.valueOf(memory.getId()));

        if(member.getAlarmToken() == null || member.getAlarmToken().isEmpty()) return;
        sendMessage(MessageDto.builder()
                .targetToken(member.getAlarmToken())
                .title(title)
                .body(body)
                .categoryType(CategoryType.valueOf("CAPSULE"))
                .dataMap(dataMap)
                .build());
    }

    @Override
    public void groupCapsuleInviteNotification(Capsule capsule, Member me, Member sender) {
        String title = "어바웃타임캡슐 - 그룹 캡슐 초대";
        String body = sender.getNickname() + "님이 당신을 캡슐에 초대했습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(capsule.getId()));
        dataMap.put("memberId", String.valueOf(me.getId()));

        if(me.getAlarmToken() == null || me.getAlarmToken().isEmpty()) return;
        sendMessage(MessageDto.builder()
                .targetToken(me.getAlarmToken())
                .title(title)
                .body(body)
                .categoryType(CategoryType.valueOf("CAPSULE"))
                .dataMap(dataMap)
                .build());
    }
}
