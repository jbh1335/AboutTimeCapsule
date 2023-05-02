package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class AroundCapsuleReq {
    int memberId;
    Double latitude;
    Double longitude;
}
