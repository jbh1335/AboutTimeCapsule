package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CapsuleCountRes {
    private int myCapsuleCnt;
    private int friendCapsuleCnt;
    private int openCapsuleCnt;

    @Builder
    public CapsuleCountRes(int myCapsuleCnt, int friendCapsuleCnt, int openCapsuleCnt) {
        this.myCapsuleCnt = myCapsuleCnt;
        this.friendCapsuleCnt = friendCapsuleCnt;
        this.openCapsuleCnt = openCapsuleCnt;
    }
}
