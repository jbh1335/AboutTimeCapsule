package com.timecapsule.capsuleservice.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CapsuleCountRes {
    private int myCapsuleCnt;
    private int friendCapsuleCnt;
    private int openCapsuleCnt;
    @JsonProperty("isNewAlarm")
    private boolean newAlarm;

    @Builder
    public CapsuleCountRes(int myCapsuleCnt, int friendCapsuleCnt, int openCapsuleCnt, boolean isNewAlarm) {
        this.myCapsuleCnt = myCapsuleCnt;
        this.friendCapsuleCnt = friendCapsuleCnt;
        this.openCapsuleCnt = openCapsuleCnt;
        this.newAlarm = isNewAlarm;
    }
}
