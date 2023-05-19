package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class AlarmTokenReq {
    private int memberId;
    private String token;
}
