package com.timecapsule.memberservice.api.response;

import com.timecapsule.memberservice.dto.FriendDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MypageRes {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private int friendCnt;
    private int friendRequestCnt;
    private List<FriendDto> friendList;
    private List<FriendDto> friendRequestList;

    @Builder
    public MypageRes(String nickname, String email, String profileImageUrl, int friendCnt, int friendRequestCnt, List<FriendDto> friendList, List<FriendDto> friendRequestList) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.friendCnt = friendCnt;
        this.friendRequestCnt = friendRequestCnt;
        this.friendList = friendList;
        this.friendRequestList = friendRequestList;
    }
}
