package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.MemberRes;
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

    // 회원 프로필 조회
    public SuccessRes<MemberRes> getMemberInfo() {
        Member findMember = findCurrentMemberId();
        MemberRes memberRes =  MemberRes.builder()
                .nickname(findMember.getNickname())
                .email(findMember.getEmail())
                .profileImageUrl(findMember.getProfileImageUrl())
                .build();
        return new SuccessRes<>(true, "내 회원정보를 조회합니다.", memberRes);
    }

    public Member findById(int id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public CommonRes updateNickname(String nickname){
        Member findMember = findCurrentMemberId();
        findMember.setNickname(nickname);
        return new CommonRes(true, "회원 닉네임을 변경했습니다.");
    }

    @Transactional(readOnly = true)
    public SuccessRes checkNicknameDuplicate(String nickname) {
        boolean isExist = memberRepository.existsByNickname(nickname); // 중복된 닉네임이 있으면 true
        return new SuccessRes(true, (isExist)? "중복된 닉네임이 있습니다." : "중복된 닉네임이 없습니다", isExist);
    }
}