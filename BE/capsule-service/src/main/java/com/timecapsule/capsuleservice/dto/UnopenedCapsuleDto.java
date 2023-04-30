package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class UnopenedCapsuleDto {
    private int capsuleId;
    private LocalDate openDate;
    private String address;
    private boolean isLocked;

    @Builder
    public UnopenedCapsuleDto(int capsuleId, LocalDate openDate, String address, boolean isLocked) {
        this.capsuleId = capsuleId;
        this.openDate = openDate;
        this.address = address;
        this.isLocked = isLocked;
    }
}
