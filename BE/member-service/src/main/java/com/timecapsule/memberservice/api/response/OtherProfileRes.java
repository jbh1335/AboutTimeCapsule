package com.timecapsule.memberservice.api.response;

import com.timecapsule.memberservice.dto.FriendDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OtherProfileRes {
    private int friendId;
    private String state;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private int friendCnt;
    private List<FriendDto> friendDtoList;

    @Builder
    public OtherProfileRes(int friendId, String state, String nickname, String email, String profileImageUrl, int friendCnt, List<FriendDto> friendDtoList) {
        this.friendId = friendId;
        this.state = state;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.friendCnt = friendCnt;
        this.friendDtoList = friendDtoList;
    }
}
