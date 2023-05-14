package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.*;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.AlarmRepository;
import com.timecapsule.capsuleservice.db.repository.MemberRepository;
import com.timecapsule.capsuleservice.dto.MessageDto;
import com.timecapsule.capsuleservice.exception.CustomException;
import com.timecapsule.capsuleservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

@Service("fcmService")
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {
    private final RedisService redisService;
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
            Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            alarmRepository.save(Alarm.builder()
                    .member(member)
                    .content(messageDto.getBody())
                    .categoryType(messageDto.getCategoryType())
                    .capsuleId(Integer.parseInt(messageDto.getDataMap().get("capsuleId")))
                    .build());

            redisService.setData("alarm_new", member.getId(), true);
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

        String alarmToken = String.valueOf(redisService.getDataValue("alarm", memory.getMember().getId()));
        if(alarmToken.equals("null")) return;

        sendMessage(MessageDto.builder()
                .targetToken(alarmToken)
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

        String alarmToken = String.valueOf(redisService.getDataValue("alarm", member.getId()));
        if(alarmToken.equals("null")) return;

        sendMessage(MessageDto.builder()
                .targetToken(alarmToken)
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

        String alarmToken = String.valueOf(redisService.getDataValue("alarm", me.getId()));
        if(alarmToken.equals("null")) return;

        sendMessage(MessageDto.builder()
                .targetToken(alarmToken)
                .title(title)
                .body(body)
                .categoryType(CategoryType.valueOf("CAPSULE"))
                .dataMap(dataMap)
                .build());
    }
}
