package com.timecapsule.capsuleservice.api.response;

import com.timecapsule.capsuleservice.dto.MapInfoDto;
import com.timecapsule.capsuleservice.dto.OpenedCapsuleDto;
import com.timecapsule.capsuleservice.dto.UnopenedCapsuleDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CapsuleListRes {
    private List<UnopenedCapsuleDto> unopenedCapsuleDtoList;
    private List<OpenedCapsuleDto> openedCapsuleDtoList;
    private List<MapInfoDto> mapInfoDtoList;
}
