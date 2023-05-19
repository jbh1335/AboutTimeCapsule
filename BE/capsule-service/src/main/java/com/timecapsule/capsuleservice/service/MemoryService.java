package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemoryService {
    SuccessRes<Integer> registMemory(List<MultipartFile> multipartFileList, MemoryRegistReq memoryRegistReq);
    SuccessRes<MemoryRes> getMemory(MemoryReq memoryReq);
    CommonRes deleteMemory(int memoryId);
    SuccessRes<ModifyMemoryRes> getModifyMemoryInfo(int memoryId);
    CommonRes modifyMemory(List<MultipartFile> multipartFileList, MemoryModifyReq memoryModifyReq);
    CommonRes registComment(CommentRegistReq commentRegistReq);
    SuccessRes<List<CommentRes>> getComment(int memoryId);
    CommonRes setGroupFirstOpenDate(GroupOpenDateReq groupOpenDateReq);
}
