package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CapsuleCountRes {
    private int myCapsuleCnt;
    private int friendCapsuleCnt;
    private int openCapsuleCnt;

}
