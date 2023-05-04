package com.timecapsule.memberservice.controller;

import com.timecapsule.memberservice.api.response.FriendRes;
import com.timecapsule.memberservice.api.response.MypageRes;
import com.timecapsule.memberservice.api.response.SuccessRes;
import com.timecapsule.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/mypage/{memberId}")
    public SuccessRes<MypageRes> getMyCapsule(@PathVariable("memberId") int memberId) {
        return memberService.getMypage(memberId);
    }

    @GetMapping("/friend/{memberId}")
    public SuccessRes<List<FriendRes>> getFriend(@PathVariable("memberId") int memberId) {
        return memberService.getFriendList(memberId);
    }
}
