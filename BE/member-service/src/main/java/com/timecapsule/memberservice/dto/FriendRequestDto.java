package com.timecapsule.memberservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendRequestDto {
    private int friendId;
    private int friendMemberId;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public FriendRequestDto(int friendId, int friendMemberId, String nickname, String profileImageUrl) {
        this.friendId = friendId;
        this.friendMemberId = friendMemberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
