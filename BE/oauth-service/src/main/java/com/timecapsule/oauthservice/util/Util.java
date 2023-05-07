package com.timecapsule.oauthservice.util;

import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.repository.MemberRepository;
import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Util {
    private final MemberRepository memberRepository;

    public Member findCurrentUser() {
        return memberRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }
}