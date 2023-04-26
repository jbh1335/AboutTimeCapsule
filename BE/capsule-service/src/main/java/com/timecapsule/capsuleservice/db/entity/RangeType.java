package com.timecapsule.capsuleservice.db.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RangeType {
    ALL, FREIND, GROUP, PRIVATE;

    @JsonCreator
    public static RangeType fromRangeType(String val) {
        for(RangeType rangeType : RangeType.values()) {
            if(rangeType.name().equals(val)) return rangeType;
        }
        throw new IllegalArgumentException("No enum constant for name " + val);
    }
}
