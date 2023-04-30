package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AroundCapsuleDto {
    private int capsuleId;
    private String memberName;
    private String address;

    @Builder
    public AroundCapsuleDto(int capsuleId, String memberName, String address) {
        this.capsuleId = capsuleId;
        this.memberName = memberName;
        this.address = address;
    }
}
