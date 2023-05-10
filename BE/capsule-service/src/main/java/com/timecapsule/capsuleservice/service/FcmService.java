package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.timecapsule.capsuleservice.api.response.CommonRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FirebaseMessaging firebaseMessaging;
    public CommonRes sendMessageTo(String targetToken, String title, String body) {
        System.out.println("service - sendMessageTo");

        Notification notification = Notification.builder().setTitle(title).setBody(body).setImage(null).build();
        Message message = Message.builder().setToken(targetToken).setNotification(notification).build();

        try {
            firebaseMessaging.send(message);
            System.out.println("알림 전송 성공");
        } catch (FirebaseMessagingException e) {
            System.out.println("알림 전송 실패");
            throw new RuntimeException(e);
        }

        return new CommonRes(true, "메세지 전송을 완료했습니다.");
    }
}
