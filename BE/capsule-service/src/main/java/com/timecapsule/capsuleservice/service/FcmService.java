package com.timecapsule.capsuleservice.service;

import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Notification;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.entity.Memory;

import java.util.HashMap;

public interface FcmService {
    void sendMessage(String targetToken, Notification notification, HashMap<String, String> dataMap);
    void commentNotification(Memory memory, Member sender);
}
