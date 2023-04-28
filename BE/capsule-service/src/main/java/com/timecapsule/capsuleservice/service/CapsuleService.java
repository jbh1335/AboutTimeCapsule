package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.CapsuleRegistReq;
import com.timecapsule.capsuleservice.api.request.MemoryRegistReq;
import com.timecapsule.capsuleservice.api.response.CapsuleListRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CapsuleService {
    SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq);
    SuccessRes<Integer> registMemory(MemoryRegistReq memoryRegistReq);
    SuccessRes<CapsuleListRes> getMyCapsule(int memberId);

}
