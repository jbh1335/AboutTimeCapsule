package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.RangeType;
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
    @PostMapping("/regist")
    public SuccessRes<Integer> registCapsule(@RequestBody CapsuleRegistReq capsuleRegistReq) {
        return capsuleService.registCapsule(capsuleRegistReq);
    }

    @PostMapping("/memory")
    public SuccessRes<Integer> registMemory(
            @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
            @RequestPart MemoryRegistReq memoryRegistReq) {
        return capsuleService.registMemory(multipartFileList, memoryRegistReq);
    }

    @GetMapping("/me/list/{memberId}")
    public SuccessRes<CapsuleListRes> getMyCapsule(@PathVariable("memberId") int memberId) {
        return capsuleService.getMyCapsule(memberId);
    }

    @GetMapping("/friend/list/{memberId}")
    public SuccessRes<CapsuleListRes> getFriendCapsule(@PathVariable("memberId") int memberId) {
        return capsuleService.getFriendCapsule(memberId);
    }

    @GetMapping("/open/list/{memberId}")
    public SuccessRes<OpenedCapsuleListRes> getOpenCapsule(@PathVariable("memberId") int memberId) {
        return capsuleService.getOpenCapsule(memberId);
    }

    @PatchMapping("/delete/{capsuleId}")
    public CommonRes deleteCapsule(@PathVariable("capsuleId") int capsuleId) {
        return capsuleService.deleteCapsule(capsuleId);
    }

    @PatchMapping("/modify/{capsuleId}/{rangeType}")
    public CommonRes modifyCapsuleRange(@PathVariable("capsuleId") int capsuleId,
                                        @PathVariable("rangeType") RangeType rangeType) {
        return capsuleService.modifyCapsuleRange(capsuleId, rangeType);
    }

    @GetMapping("/around")
    public SuccessRes<List<AroundCapsuleRes>> getAroundCapsule(@RequestBody AroundCapsuleReq aroundCapsuleReq) {
        return capsuleService.getAroundCapsule(aroundCapsuleReq);
    }

    @GetMapping("/memory")
    public SuccessRes<MemoryRes> getMemory(@RequestBody MemoryReq memoryReq) {
        return capsuleService.getMemory(memoryReq);
    }

    @PatchMapping("/memory/delete/{memoryId}")
    public CommonRes deleteMemory(@PathVariable("memoryId") int memoryId) {
        return capsuleService.deleteMemory(memoryId);
    }

    @PatchMapping("/memory/modify")
    public CommonRes modifyMemory(@RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                  @RequestPart MemoryModifyReq memoryModifyReq) {
        return capsuleService.modifyMemory(multipartFileList, memoryModifyReq);
    }

    @PostMapping("/memory/comment")
    public CommonRes registComment(@RequestBody CommentRegistReq commentRegistReq) {
        return capsuleService.registComment(commentRegistReq);
    }

    @GetMapping("/memory/comment/{memoryId}")
    public SuccessRes<List<CommentRes>> getComment(@PathVariable("memoryId") int memoryId) {
        return capsuleService.getComment(memoryId);
    }

    @GetMapping("/group/{capsuleId}")
    public SuccessRes<List<GroupMemberRes>> getGroupMember(@PathVariable("capsuleId") int capsuleId) {
        return capsuleService.getGroupMember(capsuleId);
    }

    @GetMapping("/capsuleDetail")
    public SuccessRes<CapsuleDetailRes> getCapsuleDetail(@RequestBody CapsuleDetailReq capsuleDetailReq) {
        return capsuleService.getCapsuleDetail(capsuleDetailReq);
    }

    @GetMapping("/map/capsuleDetail")
    public SuccessRes<MapCapsuleDetailRes> getMapCapsuleDetail(@RequestBody CapsuleDetailReq capsuleDetailReq) {
        return capsuleService.getMapCapsuleDetail(capsuleDetailReq);
    }
}
