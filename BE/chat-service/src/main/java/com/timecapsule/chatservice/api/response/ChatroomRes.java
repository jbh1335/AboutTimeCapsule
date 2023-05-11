package com.timecapsule.chatservice.api.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatroomRes implements Serializable {
    private String nickname;
    private String profileImageUrl;
    private String content;
    private LocalDateTime createdDate;
}
