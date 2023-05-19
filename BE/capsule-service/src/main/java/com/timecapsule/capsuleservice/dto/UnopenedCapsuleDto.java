package com.timecapsule.capsuleservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class UnopenedCapsuleDto {
    private int capsuleId;
    private LocalDate openDate;
    private String address;
    @JsonProperty("isLocked")
    private boolean locked;

    @Builder
    public UnopenedCapsuleDto(int capsuleId, LocalDate openDate, String address, boolean isLocked) {
        this.capsuleId = capsuleId;
        this.openDate = openDate;
        this.address = address;
        this.locked = isLocked;
    }
}
