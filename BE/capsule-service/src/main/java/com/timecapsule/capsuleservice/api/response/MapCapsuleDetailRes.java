package com.timecapsule.capsuleservice.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MapCapsuleDetailRes {
    private int capsuleId;
    private String memberNickname;
    private String leftTime;
    @JsonProperty("isLocked")
    private boolean locked;
    @JsonProperty("isGroup")
    private boolean group;
    private String openDate;

    @Builder
    public MapCapsuleDetailRes(int capsuleId, String memberNickname, String leftTime, boolean isLocked, boolean isGroup, String openDate) {
        this.capsuleId = capsuleId;
        this.memberNickname = memberNickname;
        this.leftTime = leftTime;
        this.locked = isLocked;
        this.group = isGroup;
        this.openDate = openDate;
    }
}
