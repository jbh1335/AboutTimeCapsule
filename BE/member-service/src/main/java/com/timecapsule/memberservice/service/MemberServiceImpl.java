package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.api.response.*;
import com.timecapsule.memberservice.db.entity.Friend;
import com.timecapsule.memberservice.db.entity.Member;
import com.timecapsule.memberservice.db.repository.FriendRepository;
import com.timecapsule.memberservice.db.repository.MemberRepository;
import com.timecapsule.memberservice.dto.FriendDto;
import com.timecapsule.memberservice.dto.FriendRequestDto;
import com.timecapsule.memberservice.exception.CustomException;
import com.timecapsule.memberservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;


@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final FcmService fcmService;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    @Override
    public SuccessRes<MypageRes> getMypage(int memberId, int otherMemberId) {
        // 내가 나의 마이페이지 조회 -> memberId, otherMemberId 같음
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Optional<Member> oOtherMember = memberRepository.findById(otherMemberId);
        Member otherMember = oOtherMember.orElseThrow(() -> new CustomException(ErrorCode.OTHERMEMBER_NOT_FOUND));

        // 친구 O -> 친구 삭제
        // 친구 X
        // 1) 아무 사이도 X -> 친구 요청
        // 2) 내가 요청을 받음 -> 요청 승인
        // 3) 내가 요청을 보냄 -> 요청 취소

        List<FriendDto> friendDtoList = new ArrayList<>();
        List<FriendRequestDto> friendRequestDtoList = new ArrayList<>();
        List<Member> friendList = friendList(otherMember);
        int friendCnt = friendList.size();

        // 다른 사람의 마이페이지 조회
        String state = "친구 요청";
        int friendId = 0;
        boolean isFriend = false;
        if(memberId != otherMemberId) {
            for(Member mem : friendList) {
                if(mem.getId() == memberId) {
                    Optional<Friend> oFriend = friendRepository.findByMemberIds(memberId, otherMemberId);
                    Friend friend = oFriend.orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

                    friendId = friend.getId();
                    state = "친구 삭제";
                    isFriend = true;
                    break;
                }
            }

            if(!isFriend) {
                Optional<Friend> oFriend1 = friendRepository.findByIsAcceptedFalseAndFromMemberIdAndToMemberId(memberId, otherMemberId);
                if(oFriend1.isPresent()) {
                    state = "요청 취소";
                    friendId = oFriend1.get().getId();
                }

                Optional<Friend> oFriend2 = friendRepository.findByIsAcceptedFalseAndFromMemberIdAndToMemberId(otherMemberId, memberId);
                if(oFriend2.isPresent()) {
                    state = "요청 승인";
                    friendId = oFriend2.get().getId();
                }
            }
        } else {
            state = "본인 마이페이지를 조회합니다.";
            member.getToMemberList().forEach(friend -> {
                if(!friend.isAccepted()) friendRequestDtoList.add(FriendRequestDto.builder()
                        .friendId(friend.getId())
                        .friendMemberId(friend.getFromMember().getId())
                        .nickname(friend.getFromMember().getNickname())
                        .profileImageUrl(friend.getFromMember().getProfileImageUrl())
                        .build());
            });
        }

        if(friendCnt > 6) friendList.subList(0, 6).clear();
        friendList.forEach(friend -> friendDtoList.add(FriendDto.builder()
                .friendMemberId(friend.getId())
                .nickname(friend.getNickname())
                .profileImageUrl(friend.getProfileImageUrl())
                .build()));


        MypageRes mypageRes = MypageRes.builder()
                .friendId(friendId)
                .otherMemberId(otherMemberId)
                .state(state)
                .nickname(otherMember.getNickname())
                .email(otherMember.getEmail())
                .profileImageUrl(otherMember.getProfileImageUrl())
                .friendCnt(friendCnt)
                .friendRequestCnt(friendRequestDtoList.size())
                .friendDtoList(friendDtoList)
                .friendRequestDtoList(friendRequestDtoList)
                .build();

        return new SuccessRes<>(true, "마이페이지 정보를 조회합니다.", mypageRes);
    }

    @Override
    public SuccessRes<List<FriendRes>> getFriendList(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<FriendRes> friendResList = new ArrayList<>();
        friendList(member).forEach(mem -> {
            Optional<Friend> oFriend = friendRepository.findByMemberIds(memberId, mem.getId());
            Friend friend = oFriend.orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

            friendResList.add(FriendRes.builder()
                    .friendId(friend.getId())
                    .friendMemberId(mem.getId())
                    .nickname(mem.getNickname())
                    .profileImageUrl(mem.getProfileImageUrl())
                    .build());
        });

        return new SuccessRes<>(true, "나의 친구 목록을 조회합니다.", friendResList);
    }

    @Override
    public SuccessRes<Integer> requestFriend(int fromMemberId, int toMemberId) {
        Optional<Member> oFromMember = memberRepository.findById(fromMemberId);
        Member fromMember = oFromMember.orElseThrow(() -> new CustomException(ErrorCode.FROMMEMBER_NOT_FOUND));

        Optional<Member> oToMember = memberRepository.findById(toMemberId);
        Member toMember = oToMember.orElseThrow(() -> new CustomException(ErrorCode.TOMEMBER_NOT_FOUND));

        Optional<Friend> oFriend = friendRepository.findByMemberIds(fromMemberId, toMemberId);
        if(oFriend.isPresent()) throw new CustomException(ErrorCode.FRIEND_ALREADY_EXISTS);

        Friend friend = friendRepository.save(Friend.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build());

        fcmService.friendRequestNotification(friend, toMember, "친구요청");
        return new SuccessRes<>(true, "친구 요청을 완료했습니다.", friend.getId());
    }

    @Override
    public CommonRes cancelRequest(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        friendRepository.deleteById(friend.getId());
        return new CommonRes(true, "친구 요청을 취소했습니다.");
    }

    @Override
    public CommonRes refuseRequest(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        friendRepository.deleteById(friend.getId());
        return new CommonRes(true, "친구 요청을 거절했습니다.");
    }

    @Override
    public CommonRes acceptRequest(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));
        friendRepository.save(Friend.of(friend, true));

        Optional<Member> oMember = memberRepository.findById(friend.getFromMember().getId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        fcmService.friendRequestNotification(friend, member, "친구요청수락");

        return new CommonRes(true, "친구 요청을 수락했습니다.");
    }

    @Override
    public CommonRes deleteFriend(int friendId) {
        Optional<Friend> oFriend = friendRepository.findById(friendId);
        Friend friend = oFriend.orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        friendRepository.deleteById(friend.getId());
        return new CommonRes(true, "친구 관계를 끊었습니다.");
    }

    @Override
    public SuccessRes<List<SearchMemberRes>> searchMember(int memberId, String nickname) {
        List<SearchMemberRes> searchMemberResList = new ArrayList<>();
        List<Member> nicknameList = memberRepository.findByNicknameContaining(nickname);

        for(Member member : nicknameList) {
            if(member.getId() == memberId) continue;
            Optional<Friend> oFriend = friendRepository.findByMemberIds(memberId, member.getId());
            int friendId = 0;
            String state = "요청";

            if(oFriend.isPresent()) {
                boolean isFriend = true;
                Optional<Friend> oFriend1 = friendRepository.findByIsAcceptedFalseAndFromMemberIdAndToMemberId(memberId, member.getId());
                if(oFriend1.isPresent()) {
                    state = "요청 취소";
                    friendId = oFriend1.get().getId();
                    isFriend = false;
                }

                Optional<Friend> oFriend2 = friendRepository.findByIsAcceptedFalseAndFromMemberIdAndToMemberId(member.getId(), memberId);
                if(oFriend2.isPresent()) {
                    state = "승인/거절";
                    friendId = oFriend2.get().getId();
                    isFriend = false;
                }

                if(isFriend) {
                    friendId = oFriend.get().getId();
                    state = "";
                }
            }

            searchMemberResList.add(SearchMemberRes.builder()
                    .friendId(friendId)
                    .memberId(member.getId())
                    .state(state)
                    .nickname(member.getNickname())
                    .profileImageUrl(member.getProfileImageUrl())
                    .build());
        }

        return new SuccessRes<>(true, "검색한 닉네임과 일치하는 멤버 목록입니다.", searchMemberResList);
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

    @Transactional
    public CommonRes updateNickname(int memberId, String nickname){
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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
