package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.MemberRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
import com.timecapsule.oauthservice.db.entity.Member;

public interface MemberService {
    SuccessRes<MemberRes> getMemberInfo();
    Member findById(int id);
    CommonRes updateNickname(String nickname);
    SuccessRes checkNicknameDuplicate(String nickname);
}