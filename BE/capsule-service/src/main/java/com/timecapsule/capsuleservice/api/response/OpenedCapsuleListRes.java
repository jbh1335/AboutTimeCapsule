package com.timecapsule.capsuleservice.api.response;

import com.timecapsule.capsuleservice.dto.MapInfoDto;
import com.timecapsule.capsuleservice.dto.OpenedCapsuleDto;
import lombok.Getter;

import java.util.List;

@Getter
public class OpenedCapsuleListRes {
    private List<OpenedCapsuleDto> openedCapsuleDtoList;
    private List<MapInfoDto> mapInfoDtoList;
}
