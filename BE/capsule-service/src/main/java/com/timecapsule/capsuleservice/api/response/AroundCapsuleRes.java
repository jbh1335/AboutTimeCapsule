package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AroundCapsuleRes {
    private int capsuleId;
    private String memberNickname;
    private String address;

    @Builder
    public AroundCapsuleRes(int capsuleId, String memberNickname, String address) {
        this.capsuleId = capsuleId;
        this.memberNickname = memberNickname;
        this.address = address;
    }
}
