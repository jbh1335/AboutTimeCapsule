package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.api.response.*;

import java.util.List;

public interface MemberService {
    SuccessRes<MypageRes> getMypage(int memberId);
    SuccessRes<List<FriendRes>> getFriendList(int memberId);
    SuccessRes<Integer> requestFriend(int fromMemberId, int toMemberId);
    CommonRes cancelRequest(int friendId);
    CommonRes refuseRequest(int friendId);
    CommonRes acceptRequest(int friendId);
    CommonRes deleteFriend(int friendId);
}
