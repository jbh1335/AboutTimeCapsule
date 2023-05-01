package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class MemoryModifyReq {
    private int memoryId;
    private String title;
    private String content;
}
