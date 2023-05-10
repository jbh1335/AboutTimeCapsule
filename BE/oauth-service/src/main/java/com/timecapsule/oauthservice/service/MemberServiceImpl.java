package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.repository.MemberRepository;
import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.exception.ErrorCode;
import com.timecapsule.oauthservice.security.UserAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    // 헤더의 토큰으로 받아온 현재 사용자 ID 반환
    public Member findCurrentMemberId() {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        log.info(memberRepository.findById(authentication.getMemberId()).toString());
        return memberRepository.findById(authentication.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public Member findById(int id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Transactional
    public CommonRes updateNickname(String nickname){
        Member findMember = findCurrentMemberId();
        if(!memberRepository.existsByNickname(nickname)) {
            findMember.updateNickname(nickname);
            return new CommonRes(true, "회원 닉네임을 수정했습니다.");
        } else {
            return new CommonRes(false, "중복된 닉네임이 있어 수정하지 못했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public SuccessRes checkNicknameDuplicate(String nickname) {
        boolean isExist = memberRepository.existsByNickname(nickname); // 중복된 닉네임이 있으면 true
        return new SuccessRes(true, (isExist)? "중복된 닉네임이 있습니다." : "중복된 닉네임이 없습니다.", isExist);
    }
}