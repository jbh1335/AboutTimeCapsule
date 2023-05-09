package com.timecapsule.memberservice.api.response;

import com.timecapsule.memberservice.dto.FriendDto;
import com.timecapsule.memberservice.dto.FriendRequestDto;
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
    private List<FriendDto> friendDtoList;
    private List<FriendRequestDto> friendRequestDtoList;

    @Builder
    public MypageRes(String nickname, String email, String profileImageUrl, int friendCnt, int friendRequestCnt, List<FriendDto> friendDtoList, List<FriendRequestDto> friendRequestDtoList) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.friendCnt = friendCnt;
        this.friendRequestCnt = friendRequestCnt;
        this.friendDtoList = friendDtoList;
        this.friendRequestDtoList = friendRequestDtoList;
    }
}
