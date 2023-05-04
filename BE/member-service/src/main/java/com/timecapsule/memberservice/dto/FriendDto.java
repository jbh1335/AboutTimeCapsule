package com.timecapsule.memberservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendDto {
    private int memberId;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public FriendDto(int memberId, String nickname, String profileImageUrl) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
