package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.api.response.CommonRes;
import com.timecapsule.memberservice.api.response.FriendRes;
import com.timecapsule.memberservice.api.response.MypageRes;
import com.timecapsule.memberservice.api.response.SuccessRes;

import java.util.List;

public interface MemberService {
    SuccessRes<MypageRes> getMypage(int memberId);
    SuccessRes<List<FriendRes>> getFriendList(int memberId);
    CommonRes requestFriend(int fromMemberId, int toMemberId);
}
