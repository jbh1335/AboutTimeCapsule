package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class AlarmReq {
    private String targetToken;
    private String title;
    private String body;
}
