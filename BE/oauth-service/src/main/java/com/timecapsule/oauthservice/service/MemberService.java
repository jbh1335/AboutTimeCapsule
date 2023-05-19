package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
import com.timecapsule.oauthservice.db.entity.Member;

public interface MemberService {
    Member findById(int id);
}