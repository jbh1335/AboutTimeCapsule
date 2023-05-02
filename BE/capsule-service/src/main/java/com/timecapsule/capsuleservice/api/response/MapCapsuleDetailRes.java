package com.timecapsule.capsuleservice.api.response;

import lombok.Getter;

@Getter
public class MapCapsuleDetailRes {
    private int capsuleId;
    private String nickname;
    private String leftTime;
    private boolean isLocked;
    private boolean isGroup;


}
