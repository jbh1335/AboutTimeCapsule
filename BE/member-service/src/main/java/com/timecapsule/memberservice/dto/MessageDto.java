package com.timecapsule.memberservice.dto;

import com.timecapsule.memberservice.db.entity.CategoryType;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class MessageDto {
    private String targetToken;
    private String title;
    private String body;
    private CategoryType categoryType;
    private HashMap<String, String> dataMap;

    @Builder
    public MessageDto(String targetToken, String title, String body, CategoryType categoryType, HashMap<String, String> dataMap) {
        this.targetToken = targetToken;
        this.title = title;
        this.body = body;
        this.categoryType = categoryType;
        this.dataMap = dataMap;
    }
}
