package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AroundCapsuleRes {
//    private List<AroundCapsuleDto> aroundCapsuleDtoList;
//    @Builder
//    public AroundCapsuleRes(List<AroundCapsuleDto> aroundCapsuleDtoList) {
//        this.aroundCapsuleDtoList = aroundCapsuleDtoList;
//    }

    private int capsuleId;
    private String memberName;
    private String address;

    @Builder
    public AroundCapsuleRes(int capsuleId, String memberName, String address) {
        this.capsuleId = capsuleId;
        this.memberName = memberName;
        this.address = address;
    }
}
