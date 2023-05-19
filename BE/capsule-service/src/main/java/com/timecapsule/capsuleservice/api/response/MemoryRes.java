package com.timecapsule.capsuleservice.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timecapsule.capsuleservice.db.entity.RangeType;
import com.timecapsule.capsuleservice.dto.MemoryDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemoryRes {
    private String capsuleTitle;
    @JsonProperty("isGroup")
    private boolean group;
    private RangeType rangeType;
    private String address;
    @JsonProperty("isFirstGroup")
    private boolean firstGroup;
    @JsonProperty("isCapsuleMine")
    private boolean capsuleMine;
    private List<MemoryDetailDto> memoryDetailDtoList;

    @Builder
    public MemoryRes(String capsuleTitle, boolean isGroup, RangeType rangeType, String address, boolean isFirstGroup, boolean isCapsuleMine, List<MemoryDetailDto> memoryDetailDtoList) {
        this.capsuleTitle = capsuleTitle;
        this.group = isGroup;
        this.rangeType = rangeType;
        this.address = address;
        this.firstGroup = isFirstGroup;
        this.capsuleMine = isCapsuleMine;
        this.memoryDetailDtoList = memoryDetailDtoList;
    }
}
