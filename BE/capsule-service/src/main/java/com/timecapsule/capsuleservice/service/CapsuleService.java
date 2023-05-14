package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.RangeType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CapsuleService {
    SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq);
    SuccessRes<CapsuleListRes> getMyCapsule(int memberId);
    SuccessRes<CapsuleListRes> getFriendCapsule(int memberId);
    SuccessRes<OpenedCapsuleListRes> getOpenCapsule(int memberId);
    CommonRes deleteCapsule(int capsuleId);
    CommonRes modifyCapsuleRange(int capsuleId, RangeType rangeType);
    SuccessRes<List<AroundCapsuleRes>> getAroundCapsule(AroundCapsuleReq aroundCapsuleReq);
    SuccessRes<List<GroupMemberRes>> getGroupMember(int capsuleId);
    SuccessRes<CapsuleDetailRes> getCapsuleDetail(CapsuleDetailReq capsuleDetailReq);
    SuccessRes<MapCapsuleDetailRes> getMapCapsuleDetail(CapsuleDetailReq capsuleDetailReq);
    SuccessRes<List<MapRes>> getMapCapsule(CapsuleDetailReq capsuleDetailReq);
    SuccessRes<CapsuleCountRes> getCapsuleCount(int memberId);
    SuccessRes<List<FriendRes>> getFriendList(int memberId);
}
