package com.timecapsule.chatservice.api.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public class ChatroomRes {
    private String nickname;
    private String profileImageUrl;
    private String content;
    private LocalDateTime createdDate;
}
