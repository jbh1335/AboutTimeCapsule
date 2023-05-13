package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifyMemoryRes {
    private int memoryId;
    private String title;
    private String content;
    private String[] imageUrl;

    @Builder
    public ModifyMemoryRes(int memoryId, String title, String content, String[] imageUrl) {
        this.memoryId = memoryId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
