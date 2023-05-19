package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class AroundCapsuleReq {
    private int memberId;
    private Double latitude;
    private Double longitude;
}
