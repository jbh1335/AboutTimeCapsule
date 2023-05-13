package com.timecapsule.capsuleservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isLocked")
    private boolean locked;
    @JsonProperty("isOpened")
    private boolean opened;
    @JsonProperty("isMemoryMine")
    private boolean memoryMine;

    @Builder
    public MemoryDetailDto(int memoryId, String nickname, String memoryTitle, String profileImageUrl, String content, String[] imageUrl, int commentCnt, LocalDate createdDate, boolean isLocked, boolean isOpened, boolean isMemoryMine) {
        this.memoryId = memoryId;
        this.nickname = nickname;
        this.memoryTitle = memoryTitle;
        this.profileImageUrl = profileImageUrl;
        this.content = content;
        this.imageUrl = imageUrl;
        this.commentCnt = commentCnt;
        this.createdDate = createdDate;
        this.locked = isLocked;
        this.opened = isOpened;
        this.memoryMine = isMemoryMine;
    }
}
