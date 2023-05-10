package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.*;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.entity.Memory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("fcmService")
public class FcmServiceImpl implements FcmService {
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
        String token = memory.getMember().getAlarmToken();

        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("capsuleId", String.valueOf(memory.getCapsule().getId()));
        dataMap.put("memoryId", String.valueOf(memory.getId()));

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        sendMessage(token, notification, dataMap);
    }
}
