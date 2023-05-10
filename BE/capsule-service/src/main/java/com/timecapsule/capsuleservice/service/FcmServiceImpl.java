package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.*;
import com.timecapsule.capsuleservice.db.entity.Capsule;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.entity.Memory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("fcmService")
public class FcmServiceImpl implements FcmService {

    @Override
    public void sendMessage(String targetToken, Notification notification, HashMap<String, String> dataMap) {
        System.out.println("service - sendMessageTo");

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(notification)
                .putAllData(dataMap)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
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

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        sendMessage(memory.getMember().getAlarmToken(), notification, dataMap);
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

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        sendMessage(member.getAlarmToken(), notification, dataMap);
    }

    @Override
    public void groupCapsuleInviteNotification(Capsule capsule, Member me, Member sender) {
        System.out.println("service - groupCapsuleInviteNotification");

        String title = "어바웃타임캡슐 - 그룹 캡슐 초대";
        String body = sender.getNickname() + "님이 당신을 캡슐에 초대했습니다.";

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(capsule));

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        sendMessage(me.getAlarmToken(), notification, dataMap);
    }
}
