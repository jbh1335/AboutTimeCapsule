package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MapCapsuleDetailRes {
    private int capsuleId;
    private String memberNickname;
    private String leftTime;
    private boolean isLocked;
    private boolean isGroup;
    private LocalDate openDate;

    @Builder
    public MapCapsuleDetailRes(int capsuleId, String memberNickname, String leftTime, boolean isLocked, boolean isGroup, LocalDate openDate) {
        this.capsuleId = capsuleId;
        this.memberNickname = memberNickname;
        this.leftTime = leftTime;
        this.isLocked = isLocked;
        this.isGroup = isGroup;
        this.openDate = openDate;
    }
}
