package com.timecapsule.capsuleservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class OpenedCapsuleDto {
    private int capsuleId;
    private LocalDate openDate;
    private String address;
    @JsonProperty("isAdded")
    private boolean added;

    @Builder
    public OpenedCapsuleDto(int capsuleId, LocalDate openDate, String address, boolean isAdded) {
        this.capsuleId = capsuleId;
        this.openDate = openDate;
        this.address = address;
        this.added = isAdded;
    }
}
