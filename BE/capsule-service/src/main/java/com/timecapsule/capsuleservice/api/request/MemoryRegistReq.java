package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemoryRegistReq {
    private int memberId;
    private int capsuleId;
    private String title;
    private String content;
    private LocalDate openDate;
}
