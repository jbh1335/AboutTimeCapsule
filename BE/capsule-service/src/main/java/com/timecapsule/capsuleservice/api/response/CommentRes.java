package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentRes {
    private int commentId;
    private int memberId;
    private String nickname;
    private String profileImageUrl;
    private String content;
    private LocalDateTime createdDate;
    @Builder
    public CommentRes(int commentId, int memberId, String nickname, String profileImageUrl, String content, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.createdDate = createdDate;
    }
}
