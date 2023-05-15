package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.RangeType;
import com.timecapsule.capsuleservice.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capsule")
public class CapsuleController {
    private final CapsuleService capsuleService;

    @PostMapping("/regist")
    public SuccessRes<Integer> registCapsule(@RequestBody CapsuleRegistReq capsuleRegistReq) {
        return capsuleService.registCapsule(capsuleRegistReq);
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

    @GetMapping("/around/popular")
    public SuccessRes<LinkedHashMap<String, List<Integer>>> getAroundPopularPlace(@RequestBody AroundCapsuleReq aroundCapsuleReq) {
        return capsuleService.getAroundPopularPlace(aroundCapsuleReq);
    }

    @GetMapping("/around/popular/list")
    public SuccessRes<CapsuleListRes> getPopularPlaceCapsule(@RequestBody PopularPlaceReq popularPlaceReq) {
        return capsuleService.getPopularPlaceCapsule(popularPlaceReq);
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

    @GetMapping("/map")
    public SuccessRes<List<MapRes>> getMapCapsule(@RequestBody CapsuleDetailReq capsuleDetailReq) {
        return capsuleService.getMapCapsule(capsuleDetailReq);
    }

    @GetMapping("/main/{memberId}")
    public SuccessRes<CapsuleCountRes> getCapsuleCount(@PathVariable("memberId") int memberId) {
        return capsuleService.getCapsuleCount(memberId);
    }

    @GetMapping("/friend/{memberId}")
    public SuccessRes<List<FriendRes>> getFriendList(@PathVariable("memberId") int memberId) {
        return capsuleService.getFriendList(memberId);
    }
}
