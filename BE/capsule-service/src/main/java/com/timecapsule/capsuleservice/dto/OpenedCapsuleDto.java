package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class OpenedCapsuleDto {
    private int capsuleId;
    private Date openDate;
    private String address;

    @Builder
    public OpenedCapsuleDto(int capsuleId, Date openDate, String address) {
        this.capsuleId = capsuleId;
        this.openDate = openDate;
        this.address = address;
    }
}
