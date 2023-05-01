package com.timecapsule.capsuleservice.api.response;

import com.timecapsule.capsuleservice.db.entity.RangeType;
import com.timecapsule.capsuleservice.dto.MemoryDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemoryRes {
    private String capsuleTitle;
    private boolean isGroup;
    private RangeType rangeType;
    private List<MemoryDetailDto> memoryDetailDtoList;

    @Builder
    public MemoryRes(String capsuleTitle, boolean isGroup, RangeType rangeType, List<MemoryDetailDto> memoryDetailDtoList) {
        this.capsuleTitle = capsuleTitle;
        this.isGroup = isGroup;
        this.rangeType = rangeType;
        this.memoryDetailDtoList = memoryDetailDtoList;
    }
}
