package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.*;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.AlarmRepository;
import com.timecapsule.capsuleservice.db.repository.MemberRepository;
import com.timecapsule.capsuleservice.db.repository.MemoryRepository;
import com.timecapsule.capsuleservice.dto.MessageDto;
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
        System.out.println("service - sendMessageTo");

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
            int memoryId = (messageDto.getDataMap().get("memoryId").isEmpty()) ? 0 : Integer.parseInt(messageDto.getDataMap().get("memoryId"));

            alarmRepository.save(Alarm.builder()
                    .member(member)
                    .content(messageDto.getBody())
                    .categoryType(messageDto.getCategoryType())
                    .memoryId(memoryId)
                    .build());
            System.out.println("알림 전송 성공");
        } catch (FirebaseMessagingException e) {
            System.out.println("알림 전송 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commentNotification(Memory memory, Member sender) {
        System.out.println("service - commentNotification");

        String title = "어바웃타임캡슐 - 댓글";
        String body = sender.getNickname() + "님이 나의 추억에 댓글을 남겼습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(memory.getCapsule().getId()));
        dataMap.put("memoryId", String.valueOf(memory.getId()));
        dataMap.put("memberId", String.valueOf(memory.getMember().getId()));

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
        System.out.println("service - openDateNotification");

        String title = "어바웃타임캡슐 - 추억 오픈";
        String body = memory.getOpenDate() + "에 생성한 추억이 " +
                member.getNickname() + "님의 방문을 기다리고 있습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(memory.getCapsule().getId()));
        dataMap.put("memoryId", String.valueOf(memory.getId()));
        dataMap.put("memberId", String.valueOf(memory.getId()));

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
        System.out.println("service - groupCapsuleInviteNotification");

        String title = "어바웃타임캡슐 - 그룹 캡슐 초대";
        String body = sender.getNickname() + "님이 당신을 캡슐에 초대했습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(capsule));
        dataMap.put("memberId", String.valueOf(me.getId()));

        sendMessage(MessageDto.builder()
                .targetToken(me.getAlarmToken())
                .title(title)
                .body(body)
                .categoryType(CategoryType.valueOf("CAPSULE"))
                .dataMap(dataMap)
                .build());
    }
}
