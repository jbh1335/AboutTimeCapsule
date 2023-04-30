package com.timecapsule.capsuleservice.api.response;

import com.timecapsule.capsuleservice.dto.AroundCapsuleDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AroundCapsuleRes {
    private List<AroundCapsuleDto> aroundCapsuleDtoList;
    @Builder
    public AroundCapsuleRes(List<AroundCapsuleDto> aroundCapsuleDtoList) {
        this.aroundCapsuleDtoList = aroundCapsuleDtoList;
    }
}
