package com.timecapsule.oauthservice.controller;

import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.MemberRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
import com.timecapsule.oauthservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController("/member")
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/me")
    public SuccessRes<MemberRes> getUser() {
        log.info("내 회원정보 조회");
        return memberService.getMemberInfo();
    }

    @GetMapping("/nickname/{nickname}")
    public CommonRes updateNickname(@PathVariable String nickname){
        return memberService.updateNickname(nickname);
    }

    @GetMapping("/nickname/{nickname}/exists")
    public SuccessRes checkNicknameDuplicate(@PathVariable String nickname){
        return memberService.checkNicknameDuplicate(nickname);
    }
}
