package com.timecapsule.capsuleservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendRes {
    private int memberId;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public FriendRes(int memberId, String nickname, String profileImageUrl) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
