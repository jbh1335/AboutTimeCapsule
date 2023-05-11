package com.timecapsule.chatservice.api.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
public class ChatroomRes implements Serializable {
    private String nickname;
    private String profileImageUrl;
    private String content;
    private LocalDateTime createdDate;
}
