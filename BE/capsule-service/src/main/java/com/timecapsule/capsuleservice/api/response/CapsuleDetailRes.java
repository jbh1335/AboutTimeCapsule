package com.timecapsule.capsuleservice.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CapsuleDetailRes {
    private String memberNickname;
    private int distance;
    private String leftTime;
    @JsonProperty("isLocked")
    private boolean locked;
    private Double latitude;
    private Double longitude;
    private String address;

    @Builder
    public CapsuleDetailRes(String memberNickname, int distance, String leftTime, boolean isLocked, Double latitude, Double longitude, String address) {
        this.memberNickname = memberNickname;
        this.distance = distance;
        this.leftTime = leftTime;
        this.locked = isLocked;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
