package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.MemberResponse;
import com.timecapsule.oauthservice.db.entity.Member;

public interface MemberService {
    MemberResponse getMemberInfo();
    Member findById(int id);
    boolean updateNickname(String nickname);
    boolean checkNicknameDuplicate(String nickname);
}