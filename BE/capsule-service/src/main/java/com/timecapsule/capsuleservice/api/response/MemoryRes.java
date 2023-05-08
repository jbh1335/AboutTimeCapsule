package com.timecapsule.capsuleservice.api.response;

import com.timecapsule.capsuleservice.db.entity.RangeType;
import com.timecapsule.capsuleservice.dto.MemoryDetailDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MemoryRes {
    private String capsuleTitle;
    private boolean isGroup;
    private RangeType rangeType;
    private String address;
    private boolean isFirstGroup;
    private boolean isMine;
    private List<MemoryDetailDto> memoryDetailDtoList;

    @Builder
    public MemoryRes(String capsuleTitle, boolean isGroup, RangeType rangeType, String address, boolean isFirstGroup, boolean isMine, List<MemoryDetailDto> memoryDetailDtoList) {
        this.capsuleTitle = capsuleTitle;
        this.isGroup = isGroup;
        this.rangeType = rangeType;
        this.address = address;
        this.isFirstGroup = isFirstGroup;
        this.isMine = isMine;
        this.memoryDetailDtoList = memoryDetailDtoList;
    }
}
