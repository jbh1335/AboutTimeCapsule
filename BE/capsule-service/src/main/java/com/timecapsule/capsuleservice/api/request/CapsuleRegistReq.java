package com.timecapsule.capsuleservice.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timecapsule.capsuleservice.db.entity.RangeType;
import lombok.Getter;

import java.util.List;

@Getter
public class CapsuleRegistReq {
    private List<Integer> memberIdList;
    private String title;
    private RangeType rangeType;
    @JsonProperty("isGroup")
    private boolean isGroup;
    private Double latitude;
    private Double longitude;
    private String address;
}
