package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.api.response.MypageRes;
import com.timecapsule.memberservice.api.response.SuccessRes;

public interface MemberService {
    SuccessRes<MypageRes> getMypage(int memberId);
}
