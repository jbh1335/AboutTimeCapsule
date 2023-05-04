package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.api.response.FriendRes;
import com.timecapsule.memberservice.api.response.MypageRes;
import com.timecapsule.memberservice.api.response.SuccessRes;
import com.timecapsule.memberservice.db.entity.Member;
import com.timecapsule.memberservice.db.repository.MemberRepository;
import com.timecapsule.memberservice.dto.FriendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    @Override
    public SuccessRes<MypageRes> getMypage(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<FriendDto> friendList = new ArrayList<>();
        List<FriendDto> friendRequestList = new ArrayList<>();
        List<Member> myFriendList = friendList(member);
        int friendCnt = myFriendList.size();
        int count = 0;

        if(friendCnt > 6) myFriendList.subList(0, 6).clear();
        myFriendList.forEach(friend -> friendList.add(FriendDto.builder()
                .memberId(friend.getId())
                .nickname(friend.getNickname())
                .profileImageUrl(friend.getProfileImageUrl())
                .build()));

        member.getToMemberList().forEach(friend -> {
            if(!friend.isAccepted()) friendRequestList.add(FriendDto.builder()
                    .memberId(friend.getFromMember().getId())
                    .nickname(friend.getFromMember().getNickname())
                    .profileImageUrl(friend.getFromMember().getProfileImageUrl())
                    .build());
        });

        MypageRes mypageRes = MypageRes.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImageUrl())
                .friendCnt(friendCnt)
                .friendRequestCnt(friendRequestList.size())
                .friendList(friendList)
                .friendRequestList(friendRequestList)
                .build();

        return new SuccessRes<>(true, "나의 마이페이지 정보를 조회합니다.", mypageRes);
    }

    @Override
    public SuccessRes<List<FriendRes>> getFriendList(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<FriendRes> friendResList = new ArrayList<>();
        friendList(member).forEach(friend -> friendResList.add(FriendRes.builder()
                .memberId(friend.getId())
                .nickname(friend.getNickname())
                .profileImageUrl(friend.getProfileImageUrl())
                .build()));

        return new SuccessRes<>(true, "나의 친구 목록을 조회합니다.", friendResList);
    }

    private List<Member> friendList(Member member) {
        List<Member> friendList = new ArrayList<>();
        member.getFromMemberList().forEach(friend -> {
            if(friend.isAccepted()) friendList.add(friend.getToMember());
        });

        member.getToMemberList().forEach(friend -> {
            if(friend.isAccepted()) friendList.add(friend.getFromMember());
        });

        return friendList;
    }
}
