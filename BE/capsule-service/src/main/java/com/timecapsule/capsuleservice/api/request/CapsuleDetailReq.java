package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class CapsuleDetailReq {
    private int capsuleId;
    private int memberId;
    private Double latitude;
    private Double longitude;
    
}
