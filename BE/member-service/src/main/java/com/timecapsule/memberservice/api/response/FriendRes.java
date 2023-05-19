package com.timecapsule.memberservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendRes {
    private int friendId;
    private int friendMemberId;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public FriendRes(int friendId, int friendMemberId, String nickname, String profileImageUrl) {
        this.friendId = friendId;
        this.friendMemberId = friendMemberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}

