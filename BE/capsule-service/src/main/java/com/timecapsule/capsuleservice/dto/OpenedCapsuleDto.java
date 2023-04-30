package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class OpenedCapsuleDto {
    private int capsuleId;
    private LocalDate openDate;
    private String address;
    private boolean isAdded;

    @Builder
    public OpenedCapsuleDto(int capsuleId, LocalDate openDate, String address, boolean isAdded) {
        this.capsuleId = capsuleId;
        this.openDate = openDate;
        this.address = address;
        this.isAdded = isAdded;
    }
}
