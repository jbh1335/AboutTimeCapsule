package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.request.MemberProfileRequest;
import com.timecapsule.oauthservice.api.response.MemberProfileResponse;
import com.timecapsule.oauthservice.api.response.MemberResponse;
import com.timecapsule.oauthservice.config.redis.RedisUtil;
import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.repository.MemberRepository;
import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.exception.ErrorCode;
import com.timecapsule.oauthservice.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final Util util;

    // 토큰으로 받아온 사용자 정보 반환
    public Member findCurrentUserId() {
        return util.findCurrentUser();
    }

    // 헤더에서 엑세스 토큰을 받아와서 회원 정보 및 회원 프로필이 수정 되어있는지 확인 후 반환
    public MemberResponse findValidateProfileSaveUser() {
        Member findUser = findCurrentUserId();
        return MemberResponse.builder()
                .id(findUser.getId())
                .nickName(findUser.getNickName())
                .email(findUser.getEmail())
                .roleType(findUser.getRoleType())
                .profileImageUrl(findUser.getProfileImageUrl())
                .build();
    }


    // 회원 프로필 조회
    public Member findById(int id){
        return getFindById(id);
    }

    // 회원 프로필 생성
    @Transactional
    public MemberProfileResponse updateProfile(MemberProfileRequest memberProfileRequest) throws IOException {
        Member findUser = findCurrentUserId();
        findUser.updateProfile(
                memberProfileRequest.getEmail(),
                memberProfileRequest.getProfileImageUrl());

        return new MemberProfileResponse(findUser.getProfileImageUrl());
    }

    private Member getFindById(int userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

}