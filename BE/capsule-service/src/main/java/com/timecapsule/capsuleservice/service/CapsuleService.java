package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.RangeType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CapsuleService {
    SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq);
    SuccessRes<Integer> registMemory(List<MultipartFile> multipartFileList, MemoryRegistReq memoryRegistReq);
    SuccessRes<CapsuleListRes> getMyCapsule(int memberId);
    SuccessRes<CapsuleListRes> getFriendCapsule(int memberId);
    SuccessRes<OpenedCapsuleListRes> getOpenCapsule(int memberId);
    CommonRes deleteCapsule(int capsuleId);
    CommonRes modifyCapsuleRange(int capsuleId, RangeType rangeType);
    SuccessRes<List<AroundCapsuleRes>> getAroundCapsule(AroundCapsuleReq aroundCapsuleReq);
    SuccessRes<MemoryRes> getMemory(MemoryReq memoryReq);
    CommonRes deleteMemory(int memoryId);
    CommonRes modifyMemory(List<MultipartFile> multipartFileList, MemoryModifyReq memoryModifyReq);
    CommonRes registComment(CommentRegistReq commentRegistReq);
    SuccessRes<List<CommentRes>> getComment(int memoryId);
    SuccessRes<List<GroupMemberRes>> getGroupMember(int capsuleId);

}
