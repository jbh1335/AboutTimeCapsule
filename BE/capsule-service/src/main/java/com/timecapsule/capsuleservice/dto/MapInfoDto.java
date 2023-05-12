package com.timecapsule.capsuleservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MapInfoDto {
    private int capsuleId;
    private Double latitude;
    private Double longitude;
    @JsonProperty("isOpened")
    private boolean opened;
    @JsonProperty("isLocked")
    private boolean locked;

    @Builder
    public MapInfoDto(int capsuleId, Double latitude, Double longitude, boolean isOpened, boolean isLocked) {
        this.capsuleId = capsuleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.opened = isOpened;
        this.locked = isLocked;
    }
}
