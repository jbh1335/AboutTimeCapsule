package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MapRes {
    private int capsuleId;
    private boolean isLocked;
    private boolean isMine;
    private boolean isAllowedDistance;
    private Double latitude;
    private Double longitude;

    @Builder
    public MapRes(int capsuleId, boolean isLocked, boolean isMine, boolean isAllowedDistance, Double latitude, Double longitude) {
        this.capsuleId = capsuleId;
        this.isLocked = isLocked;
        this.isMine = isMine;
        this.isAllowedDistance = isAllowedDistance;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
