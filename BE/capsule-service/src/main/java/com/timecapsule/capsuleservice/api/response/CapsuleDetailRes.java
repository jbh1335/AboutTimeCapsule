package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CapsuleDetailRes {
    private String nickname;
    private int distance;
    private String leftTime;
    private boolean isLocked;
    private Double latitude;
    private Double longitude;
    private String address;

    @Builder
    public CapsuleDetailRes(String nickname, int distance, String leftTime, boolean isLocked, Double latitude, Double longitude, String address) {
        this.nickname = nickname;
        this.distance = distance;
        this.leftTime = leftTime;
        this.isLocked = isLocked;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
