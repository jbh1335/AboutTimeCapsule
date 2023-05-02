package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemoryDetailDto {
    private int memoryId;
    private String nickname;
    private String memoryTitle;
    private String profileImageUrl;
    private String content;
    private String[] imageUrl;
    private int commentCnt;
    private LocalDate createdDate;
    private boolean isLocked;
    private boolean isOpened;

    @Builder
    public MemoryDetailDto(int memoryId, String nickname, String memoryTitle, String profileImageUrl, String content, String[] imageUrl, int commentCnt, LocalDate createdDate, boolean isLocked, boolean isOpened) {
        this.memoryId = memoryId;
        this.nickname = nickname;
        this.memoryTitle = memoryTitle;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.imageUrl = imageUrl;
        this.commentCnt = commentCnt;
        this.createdDate = createdDate;
        this.isLocked = isLocked;
        this.isOpened = isOpened;
    }
}
