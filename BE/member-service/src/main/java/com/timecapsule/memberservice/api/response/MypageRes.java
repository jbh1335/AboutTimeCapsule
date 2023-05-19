package com.timecapsule.memberservice.api.response;

import com.timecapsule.memberservice.dto.FriendDto;
import com.timecapsule.memberservice.dto.FriendRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MypageRes {
    private int friendId;
    private int otherMemberId;
    private String state;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private int friendCnt;
    private int friendRequestCnt;
    private List<FriendDto> friendDtoList;
    private List<FriendRequestDto> friendRequestDtoList;

    @Builder
    public MypageRes(int friendId, int otherMemberId, String state, String nickname, String email, String profileImageUrl, int friendCnt, int friendRequestCnt, List<FriendDto> friendDtoList, List<FriendRequestDto> friendRequestDtoList) {
        this.friendId = friendId;
        this.otherMemberId = otherMemberId;
        this.state = state;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.friendCnt = friendCnt;
        this.friendRequestCnt = friendRequestCnt;
        this.friendDtoList = friendDtoList;
        this.friendRequestDtoList = friendRequestDtoList;
    }
}
