package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GroupOpenDateReq {
    private int capsuleId;
    private LocalDate openDate;
}
