package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.request.CapsuleRegistReq;
import com.timecapsule.capsuleservice.api.request.MemoryRegistReq;
import com.timecapsule.capsuleservice.api.response.CapsuleListRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@Api(value = "캡슐 API", tags = {"Capsule"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capsule")
public class CapsuleController {
    private final CapsuleService capsuleService;

//    @ApiOperation(value = "캡슐 등록", notes = "나의 캡슐 등록하기")
    @PostMapping
    public SuccessRes<Integer> registCapsule(@RequestBody CapsuleRegistReq capsuleRegistReq) {
        return capsuleService.registCapsule(capsuleRegistReq);
    }

    @PostMapping("/memory")
    public SuccessRes<Integer> registMemory(@RequestBody MemoryRegistReq memoryRegistReq) {
        return capsuleService.registMemory(memoryRegistReq);
    }

//    @PostMapping("/image")
//    public SuccessRes<Integer> imageupload(@RequestPart(value="file",required = false)  List<MultipartFile> multipartFile) {
//        System.out.println("여기");
//        return capsuleService.registMemory(multipartFile);
//    }

    @GetMapping("/me/list/{memberId}")
    public SuccessRes<CapsuleListRes> getMyCapsule(@PathVariable("memberId") int memberId) {
        return capsuleService.getMyCapsule(memberId);
    }

}
