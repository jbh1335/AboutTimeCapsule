package com.timecapsule.memberservice.api.response;

import com.timecapsule.memberservice.dto.FriendDto;
import com.timecapsule.memberservice.dto.RequestDto;
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
    private List<RequestDto> requestDtoList;

    @Builder
    public MypageRes(String nickname, String email, String profileImageUrl, int friendCnt, int friendRequestCnt, List<FriendDto> friendDtoList, List<RequestDto> requestDtoList) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.friendCnt = friendCnt;
        this.friendRequestCnt = friendRequestCnt;
        this.friendDtoList = friendDtoList;
        this.requestDtoList = requestDtoList;
    }
}
