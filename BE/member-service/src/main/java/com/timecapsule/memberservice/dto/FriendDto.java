package com.timecapsule.memberservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendDto {
    private int friendMemberId;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public FriendDto(int friendMemberId, String nickname, String profileImageUrl) {
        this.friendMemberId = friendMemberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
