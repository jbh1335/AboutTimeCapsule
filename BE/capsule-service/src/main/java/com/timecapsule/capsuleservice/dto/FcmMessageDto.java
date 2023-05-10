package com.timecapsule.capsuleservice.dto;

import com.google.firebase.messaging.Message;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmMessageDto {
    private boolean validateOnly;
    private Message message;

    @Getter
    @Builder
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Getter
    @Builder
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
