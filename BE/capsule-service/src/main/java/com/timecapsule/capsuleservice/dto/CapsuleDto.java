package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CapsuleDto {
    private List<UnopenedCapsuleDto> unopenedCapsuleDtoList;
    private List<OpenedCapsuleDto> openedCapsuleDtoList;
    private List<MapInfoDto> mapInfoDtoList;
    private List<Integer> capsuleIdList;

    @Builder
    public CapsuleDto(List<UnopenedCapsuleDto> unopenedCapsuleDtoList, List<OpenedCapsuleDto> openedCapsuleDtoList, List<MapInfoDto> mapInfoDtoList, List<Integer> capsuleIdList) {
        this.unopenedCapsuleDtoList = unopenedCapsuleDtoList;
        this.openedCapsuleDtoList = openedCapsuleDtoList;
        this.mapInfoDtoList = mapInfoDtoList;
        this.capsuleIdList = capsuleIdList;
    }
}
