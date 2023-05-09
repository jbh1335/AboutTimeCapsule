package com.timecapsule.capsuleservice.dto;

import lombok.Getter;

@Getter
public class RequestDto {
    private String targetToken;
    private String title;
    private String body;
}
