package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.CapsuleRegistReq;
import com.timecapsule.capsuleservice.api.response.CapsuleListRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;

public interface CapsuleService {
    SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq);
    SuccessRes<CapsuleListRes> getMyCapsule(int memberId);
}
