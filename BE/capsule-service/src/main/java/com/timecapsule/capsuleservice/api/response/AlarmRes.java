package com.timecapsule.capsuleservice.api.response;

import com.timecapsule.capsuleservice.db.entity.CategoryType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlarmRes {
    private int alarmId;
    private int memberId;
    private int capsuleId;
    private String content;
    private CategoryType categoryType;

    @Builder
    public AlarmRes(int alarmId, int memberId, int capsuleId, String content, CategoryType categoryType) {
        this.alarmId = alarmId;
        this.memberId = memberId;
        this.capsuleId = capsuleId;
        this.content = content;
        this.categoryType = categoryType;
    }
}
