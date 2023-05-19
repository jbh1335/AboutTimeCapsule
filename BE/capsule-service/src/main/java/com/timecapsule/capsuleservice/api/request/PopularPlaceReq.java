package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PopularPlaceReq {
    private int memberId;
    private List<Integer> capsuleIdList;
}
