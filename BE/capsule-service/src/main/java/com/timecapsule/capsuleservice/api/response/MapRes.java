package com.timecapsule.capsuleservice.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MapRes {
    private int capsuleId;
    @JsonProperty("isLocked")
    private boolean locked;
    @JsonProperty("isMine")
    private boolean mine;
    @JsonProperty("isAllowedDistance")
    private boolean allowedDistance;
    private Double latitude;
    private Double longitude;

    @Builder
    public MapRes(int capsuleId, boolean isLocked, boolean isMine, boolean isAllowedDistance, Double latitude, Double longitude) {
        this.capsuleId = capsuleId;
        this.locked = isLocked;
        this.mine = isMine;
        this.allowedDistance = isAllowedDistance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
