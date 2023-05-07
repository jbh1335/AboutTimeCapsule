package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.api.response.*;
import com.timecapsule.memberservice.db.entity.Friend;
import com.timecapsule.memberservice.db.entity.Member;
import com.timecapsule.memberservice.db.repository.FriendRepository;
import com.timecapsule.memberservice.db.repository.MemberRepository;
import com.timecapsule.memberservice.dto.FriendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

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

    @Override
    public SuccessRes<Integer> requestFriend(int fromMemberId, int toMemberId) {
        Optional<Member> oFromMember = memberRepository.findById(fromMemberId);
        Member fromMember = oFromMember.orElseThrow(() -> new IllegalArgumentException("fromMember doesn't exist"));

        Optional<Member> oToMember = memberRepository.findById(toMemberId);
        Member toMember = oToMember.orElseThrow(() -> new IllegalArgumentException("toMember doesn't exist"));

        int friendId = friendRepository.save(Friend.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build()).getId();

        return new SuccessRes<>(true, "친구 요청을 완료했습니다.", friendId);
    }

    @Override
    public CommonRes cancelRequest(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new IllegalArgumentException("friend doesn't exist"));

        friendRepository.deleteById(friend.getId());
        return new CommonRes(true, "친구 요청을 취소했습니다.");
    }

    @Override
    public CommonRes refuseRequest(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new IllegalArgumentException("friend doesn't exist"));

        friendRepository.deleteById(friend.getId());
        return new CommonRes(true, "친구 요청을 거절했습니다.");
    }

    @Override
    public CommonRes acceptRequest(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new IllegalArgumentException("friend doesn't exist"));

        friendRepository.save(Friend.of(friend, true));
        return new CommonRes(true, "친구 요청을 수락했습니다.");
    }

    @Override
    public SuccessRes<List<RequestRes>> getRequestList(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<RequestRes> requestResList = new ArrayList<>();
        member.getToMemberList().forEach(friend -> {
            if(!friend.isAccepted()) requestResList.add(RequestRes.builder()
                    .friendId(friend.getId())
                    .memberId(friend.getFromMember().getId())
                    .nickname(friend.getFromMember().getNickname())
                    .profileImageUrl(friend.getFromMember().getProfileImageUrl())
                    .build());
        });

        return new SuccessRes<>(true, "제가 받은 친구 요청을 조회합니다.", requestResList);
    }

    private List<Member> friendList(Member member) {
        List<Member> friendList = new ArrayList<>();
        member.getFromMemberList().forEach(friend -> {
            if(friend.isAccepted()) friendList.add(friend.getToMember());
        });

        member.getToMemberList().forEach(friend -> {
            if(friend.isAccepted()) friendList.add(friend.getFromMember());
        });

        friendList.sort(Comparator.comparing(Member::getNickname));
        return friendList;
    }
}
