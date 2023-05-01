package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.*;
import com.timecapsule.capsuleservice.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("capsuleService")
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    private final AwsS3Service awsS3Service;
    private final DistanceService distanceService;
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final MemoryOpenMemberRepository memoryOpenMemberRepository;
    private final MemoryRepository memoryRepository;
    private final FriendRepository friendRepository;
    private final CommentRepository commentRepository;

    @Override
    public SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq) {
        Capsule capsule = Capsule.builder()
                .title(capsuleRegistReq.getTitle())
                .rangeType(capsuleRegistReq.getRangeType())
                .isGroup(capsuleRegistReq.isGroup())
                .latitude(capsuleRegistReq.getLatitude())
                .longitude(capsuleRegistReq.getLongitude())
                .address(capsuleRegistReq.getAddress())
                .build();

        Capsule newCapsule = capsuleRepository.save(capsule);

        for(Integer id : capsuleRegistReq.getMemberIdList()) {
            Optional<Member> oMember = memberRepository.findById(id);
            Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

            capsuleMemberRepository.save(CapsuleMember.builder().member(member).capsule(newCapsule).build());
        }

        return new SuccessRes<>(true, "캡슐 등록을 완료했습니다.", newCapsule.getId());
    }

    @Override
    public SuccessRes<Integer> registMemory(List<MultipartFile> multipartFileList, MemoryRegistReq memoryRegistReq) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(memoryRegistReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new IllegalArgumentException("capsule doesn't exist"));

        Optional<Member> oMember = memberRepository.findById(memoryRegistReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        String image = "";
        if(!multipartFileList.get(0).isEmpty()) image = awsS3Service.uploadFile(multipartFileList);

        Memory memory = Memory.builder()
                .capsule(capsule)
                .member(member)
                .title(memoryRegistReq.getTitle())
                .content(memoryRegistReq.getContent())
                .image(image)
                .isDeleted(false)
                .isLocked(true)
                .openDate(memoryRegistReq.getOpenDate())
                .build();

        int memoryId = memoryRepository.save(memory).getId();

        return new SuccessRes<>(true, "추억 등록을 완료했습니다.", memoryId);
    }

    @Override
    public SuccessRes<CapsuleListRes> getMyCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        for(CapsuleMember capsuleMember : member.getCapsuleMemberList()) {
            Capsule capsule = capsuleMember.getCapsule();
            if(capsule.isDeleted()) continue;

            int capsuleId = capsule.getId();
            // 캡슐에 대한 열람 기록이 있는지 확인
            boolean isExisted = memoryOpenMemberRepository.existsByCapsuleIdAndMemberId(capsuleId, memberId);
            boolean isLocked = false;

            int memorySize = capsule.getMemoryList().size();
            LocalDate openDate = null;
            if(memorySize != 0) openDate = capsule.getMemoryList().get(memorySize-1).getOpenDate();

            // 미열람
            if(!isExisted) {
                // 잠김 O
                if(memorySize > 0 && LocalDate.now().isBefore(openDate)) isLocked = true;

                unopenedCapsuleDtoList.add(UnopenedCapsuleDto.builder()
                        .capsuleId(capsuleId)
                        .openDate(openDate)
                        .address(capsule.getAddress())
                        .isLocked(isLocked)
                        .build());
            } else {
                // 열람
                int count = memoryOpenMemberRepository.countByCapsuleIdAndMemberId(capsuleId, memberId);
                boolean isAdded = false;
                if(memorySize != count) isAdded = true;

                openedCapsuleDtoList.add(OpenedCapsuleDto.builder()
                        .capsuleId(capsuleId)
                        .openDate(openDate)
                        .address(capsule.getAddress())
                        .isAdded(isAdded)
                        .build());
            }

            mapInfoDtoList.add(MapInfoDto.builder()
                    .capsuleId(capsuleId)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .isOpened(isExisted)
                    .isLocked(isLocked)
                    .build());
        }

//        Collections.sort(unopenedCapsuleDtoList, (o1, o2) -> o1.getOpenDate().compareTo(o2.getOpenDate()));
//        Collections.sort(openedCapsuleDtoList, (o1, o2) -> o1.getOpenDate().compareTo(o2.getOpenDate()));
        Collections.sort(unopenedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));
        Collections.sort(openedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));

        CapsuleListRes capsuleListRes = CapsuleListRes.builder()
                .unopenedCapsuleDtoList(unopenedCapsuleDtoList)
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();

        return new SuccessRes<>(true, "나의 캡슐 목록을 조회합니다.", capsuleListRes);
    }

    @Override
    public SuccessRes<CapsuleListRes> getFriendCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        // 이어서
        return null;
    }

    @Override
    public SuccessRes<OpenedCapsuleListRes> getOpenCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        for(MemoryOpenMember memoryOpenMember : member.getMemoryOpenMemberList()) {
            Capsule capsule = memoryOpenMember.getCapsule();
            if(capsule.isDeleted()) continue;
            int capsuleId = capsule.getId();

            int memorySize = capsule.getMemoryList().size();
            LocalDate openDate = null;
            if(memorySize != 0) openDate = capsule.getMemoryList().get(memorySize-1).getOpenDate();

            int count = memoryOpenMemberRepository.countByCapsuleIdAndMemberId(capsuleId, memberId);
            boolean isAdded = false;
            if(memorySize != count) isAdded = true;

            openedCapsuleDtoList.add(OpenedCapsuleDto.builder()
                    .capsuleId(capsuleId)
                    .openDate(openDate)
                    .address(capsule.getAddress())
                    .isAdded(isAdded)
                    .build());

            mapInfoDtoList.add(MapInfoDto.builder()
                    .capsuleId(capsuleId)
                    .latitude(capsule.getLatitude())
                    .longitude(capsule.getLongitude())
                    .isOpened(true)
                    .isLocked(false)
                    .build());

        }

        Collections.sort(openedCapsuleDtoList, Comparator.comparing(o -> (o.getOpenDate() == null ? LocalDate.MIN : o.getOpenDate())));

        OpenedCapsuleListRes openedCapsuleListRes = OpenedCapsuleListRes.builder()
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();
        return new SuccessRes<>(true, "나의 방문 기록을 조회합니다.", openedCapsuleListRes);
    }

    @Override
    public CommonRes deleteCapsule(int capsuleId) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
        Capsule capsule = oCapsule.orElseThrow(() -> new IllegalArgumentException("capsule doesn't exist"));

        for(Memory memory : capsule.getMemoryList()) {
            memory.getCommentList().forEach(comment -> commentRepository.save(Comment.of(comment, true)));
            memoryRepository.save(Memory.of(memory, true));
        }

        capsuleRepository.save(Capsule.of(capsule, true));

        return new CommonRes(true, "캡슐 삭제를 완료했습니다.");
    }

    @Override
    public CommonRes modifyCapsuleRange(int capsuleId, RangeType rangeType) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
        Capsule capsule = oCapsule.orElseThrow(() -> new IllegalArgumentException("capsule doesn't exist"));

        capsuleRepository.save(Capsule.of(capsule, rangeType));
        return new CommonRes(true, "캡슐의 공개 범위를 변경했습니다.");
    }

    @Override
    public SuccessRes<List<AroundCapsuleRes>> getAroundCapsule(AroundCapsuleReq aroundCapsuleReq) {
        // 그냥 전체 공개로 설정한 사람들의 캡슐 조회
        // 오픈 기간 지났고 내가 열람한 적 없는 주변 1km 이내에 있는 모든 캡슐 조회

        List<AroundCapsuleRes> aroundCapsuleResList = new ArrayList<>();
        List<Capsule> aroundCapsuleList = capsuleRepository.findAroundCapsule(aroundCapsuleReq.getLatitude(), aroundCapsuleReq.getLongitude());

        for(Capsule capsule : aroundCapsuleList) {
            if(capsule.isDeleted()) continue;
            // 내 권한 X
            boolean isMember = capsuleMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), aroundCapsuleReq.getMemberId());
            if(isMember) continue;

            if(!capsule.getRangeType().equals("ALL")) continue;

            String memberName = capsule.getCapsuleMemberList().get(0).getMember().getName();
            int memberSize = capsule.getCapsuleMemberList().size() - 1;
            if(memberSize > 1) memberName += (" 외 " + memberSize + "명");

            aroundCapsuleResList.add(AroundCapsuleRes.builder()
                    .capsuleId(capsule.getId())
                    .memberName(memberName)
                    .address(capsule.getAddress())
                    .build());
        }

        return new SuccessRes<>(true, "내 주변 캡슐을 조회합니다.", aroundCapsuleResList);
    }

    @Override
    public SuccessRes<MemoryRes> getMemory(MemoryReq memoryReq) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(memoryReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new IllegalArgumentException("capsule doesn't exist"));

        List<MemoryDetailDto> memoryDetailDtoList = new ArrayList<>();
        for(Memory memory : capsule.getMemoryList()) {
            if(memory.isDeleted()) continue;

            String[] imageUrl = memory.getImage().split("#");
            int commentCnt = commentRepository.findAllByMemoryId(memory.getId()).size();

            // 잠김 X, 내가 오픈한 적 있는 추억은 계속 오픈O 거리상관X -> isOpened = true, isLocked = false
            // 잠김 X, 오픈한 적 없는데 거리가 가까우면 가능 오픈O -> isOpened = true, isLocked = false + memoryOpenMember에 추가
            // 잠김 X, 오픈한 적 없는데 거리도 멀면 불가능 오픈X -> isOpened = false, isLocked = false
            // 잠김 O, 그냥 불가능 isOpened = false, isLocked = true

            boolean isOpened = memoryOpenMemberRepository.existsByMemoryIdAndMemberId(memory.getId(), memoryReq.getMemberId());
            boolean isNowOpened = false;
            boolean isLocked = false;
            if(LocalDate.now().isBefore(memory.getOpenDate())) isLocked = true;
            else {
                if(isOpened) {
                    isNowOpened = true;
                } else {
                    // 거리 계산
                    double distance = distanceService.distance(memoryReq.getLatitude(), memoryReq.getLongitude(),
                            capsule.getLatitude(), capsule.getLongitude(), "meter");
                    if(distance <= 100) {
                        isNowOpened = true;

                        Optional<Member> oMember = memberRepository.findById(memoryReq.getMemberId());
                        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

                        memoryOpenMemberRepository.save(MemoryOpenMember.builder()
                                .capsule(capsule)
                                .member(member)
                                .memory(memory)
                                .build());
                    }
                }
            }

            memoryDetailDtoList.add(MemoryDetailDto.builder()
                    .memoryId(memory.getId())
                    .nickname(memory.getMember().getNickname())
                    .memoryTitle(memory.getTitle())
                    .profileImageUrl(memory.getMember().getProfileImageUrl())
                    .content(memory.getContent())
                    .imageUrl(memory.getImage().split("#"))
                    .commentCnt(commentCnt)
                    .createdDate(memory.getCreatedDate().toLocalDate())
                    .isLocked(isLocked)
                    .isOpened(isNowOpened)
                    .build());
        }

        MemoryRes memoryRes = MemoryRes.builder()
                .capsuleTitle(capsule.getTitle())
                .isGroup(capsule.isGroup())
                .rangeType(capsule.getRangeType())
                .memoryDetailDtoList(memoryDetailDtoList)
                .build();

        return new SuccessRes<>(true, "해당 캡슐의 추억을 조회합니다.", memoryRes);
    }

    @Override
    public CommonRes deleteMemory(int memoryId) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryId);
        Memory memory = oMemory.orElseThrow(() -> new IllegalArgumentException("memory doesn't exist"));

        memory.getCommentList().forEach(comment -> commentRepository.save(Comment.of(comment, true)));
        memoryRepository.save(Memory.of(memory, true));

        return new CommonRes(true, "추억 삭제를 완료했습니다.");
    }

    @Override
    public CommonRes modifyMemory(List<MultipartFile> multipartFileList, MemoryModifyReq memoryModifyReq) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryModifyReq.getMemoryId());
        Memory memory = oMemory.orElseThrow(() -> new IllegalArgumentException("memory doesn't exist"));

        String image = memory.getImage();
        if(!multipartFileList.get(0).isEmpty()) image = awsS3Service.uploadFile(multipartFileList);

        memoryRepository.save(Memory.of(memory, memoryModifyReq.getTitle(), memoryModifyReq.getContent(), image));
        return new CommonRes(true, "추억 수정을 완료했습니다.");
    }

    @Override
    public CommonRes registComment(CommentRegistReq commentRegistReq) {
        Optional<Member> oMember = memberRepository.findById(commentRegistReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        Optional<Memory> oMemory = memoryRepository.findById(commentRegistReq.getMemoryId());
        Memory memory = oMemory.orElseThrow(() -> new IllegalArgumentException("memory doesn't exist"));

        commentRepository.save(Comment.builder()
                .member(member)
                .memory(memory)
                .content(commentRegistReq.getContent())
                .isDeleted(false)
                .build());

        return new CommonRes(true, "댓글 등록을 완료했습니다.");
    }

    @Override
    public SuccessRes<List<CommentRes>> getComment(int memoryId) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryId);
        Memory memory = oMemory.orElseThrow(() -> new IllegalArgumentException("memory doesn't exist"));

        List<CommentRes> commentResList = new ArrayList<>();
        for(Comment comment : memory.getCommentList()) {
            if(comment.isDeleted()) continue;
            Member member = comment.getMember();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm");

            commentResList.add(CommentRes.builder()
                    .commentId(comment.getId())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImageUrl(member.getProfileImageUrl())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate().format(dateTimeFormatter))
                    .build());
        }

        return new SuccessRes<>(true, "댓글 목록을 조회합니다.", commentResList);
    }

    @Override
    public SuccessRes<List<GroupMemberRes>> getGroupMember(int capsuleId) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(capsuleId);
        Capsule capsule = oCapsule.orElseThrow(() -> new IllegalArgumentException("capsule doesn't exist"));

        List<GroupMemberRes> groupMemberResList = new ArrayList<>();
        for(CapsuleMember capsuleMember : capsule.getCapsuleMemberList()) {
            Member member = capsuleMember.getMember();

            groupMemberResList.add(GroupMemberRes.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImageUrl(member.getProfileImageUrl())
                    .build());
        }

        return new SuccessRes<>(true, "그룹에 해당되는 멤버 목록을 조회합니다.", groupMemberResList);
    }
}
