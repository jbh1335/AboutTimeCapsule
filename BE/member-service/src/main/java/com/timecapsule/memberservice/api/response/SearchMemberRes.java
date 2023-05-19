package com.timecapsule.memberservice.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchMemberRes {
    private int friendId;
    private int memberId;
    private String state;
    private String nickname;
    private String profileImageUrl;

    @Builder
    public SearchMemberRes(int friendId, int memberId, String state, String nickname, String profileImageUrl) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.state = state;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
