package com.timecapsule.capsuleservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MapInfoDto {
    private int capsuleId;
    private Double latitude;
    private Double longitude;
    private boolean isOpened;
    private boolean isLocked;

    @Builder
    public MapInfoDto(int capsuleId, Double latitude, Double longitude, boolean isOpened, boolean isLocked) {
        this.capsuleId = capsuleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOpened = isOpened;
        this.isLocked = isLocked;
    }
}
