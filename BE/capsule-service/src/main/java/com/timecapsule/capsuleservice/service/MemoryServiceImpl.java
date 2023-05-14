package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.*;
import com.timecapsule.capsuleservice.dto.MemoryDetailDto;
import com.timecapsule.capsuleservice.exception.CustomException;
import com.timecapsule.capsuleservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("memoryService")
@RequiredArgsConstructor
public class MemoryServiceImpl implements MemoryService {
    private final AwsS3Service awsS3Service;
    private final DistanceService distanceService;
    private final FcmService fcmService;
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final MemoryOpenMemberRepository memoryOpenMemberRepository;
    private final MemoryRepository memoryRepository;
    private final CommentRepository commentRepository;

    @Override
    public SuccessRes<Integer> registMemory(List<MultipartFile> multipartFileList, MemoryRegistReq memoryRegistReq) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(memoryRegistReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        Optional<Member> oMember = memberRepository.findById(memoryRegistReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String image = "";
        if(!multipartFileList.get(0).isEmpty()) image = awsS3Service.uploadFile(multipartFileList);

        boolean isLocked = false;
        if(memoryRegistReq.getOpenDate() != null && LocalDate.now().isBefore(memoryRegistReq.getOpenDate())) isLocked = true;

        Memory memory = Memory.builder()
                .capsule(capsule)
                .member(member)
                .title(memoryRegistReq.getTitle())
                .content(memoryRegistReq.getContent())
                .image(image)
                .isDeleted(false)
                .isLocked(isLocked)
                .openDate(memoryRegistReq.getOpenDate())
                .build();

        int memoryId = memoryRepository.save(memory).getId();

        return new SuccessRes<>(true, "추억 등록을 완료했습니다.", memoryId);
    }

    @Override
    public SuccessRes<MemoryRes> getMemory(MemoryReq memoryReq) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(memoryReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        boolean isFirstGroup = capsule.isGroup();
        boolean isCapsuleMine = capsuleMemberRepository.existsByCapsuleIdAndMemberId(memoryReq.getCapsuleId(), memoryReq.getMemberId());

        List<MemoryDetailDto> memoryDetailDtoList = new ArrayList<>();
        for(Memory memory : capsule.getMemoryList()) {
            if(memory.isDeleted()) continue;
            if(capsule.isGroup() && memory.getOpenDate() != null) isFirstGroup = false;

            String[] imageUrl = memory.getImage().split("#");
            int commentCnt = commentRepository.findAllByMemoryId(memory.getId()).size();

            // 잠김 X, 내가 오픈한 적 있는 추억은 계속 오픈O 거리상관X -> isOpened = true, isLocked = false
            // 잠김 X, 오픈한 적 없는데 거리가 가까우면 가능 오픈O -> isOpened = true, isLocked = false + memoryOpenMember에 추가
            // 잠김 X, 오픈한 적 없는데 거리도 멀면 불가능 오픈X -> isOpened = false, isLocked = false
            // 잠김 O, 그냥 불가능 isOpened = false, isLocked = true

            boolean isMemoryMine = (memoryReq.getMemberId() == memory.getMember().getId());
            boolean isOpened = memoryOpenMemberRepository.existsByMemoryIdAndMemberId(memory.getId(), memoryReq.getMemberId());
            boolean isNowOpened = false;
            boolean isLocked = false;
            if(memory.getOpenDate() != null && LocalDate.now().isBefore(memory.getOpenDate())) isLocked = true;

            else {
                if(isOpened) {
                    isNowOpened = true;
                } else {
                    // 거리 계산
                    int distance = (int) distanceService.distance(memoryReq.getLatitude(), memoryReq.getLongitude(),
                            capsule.getLatitude(), capsule.getLongitude(), "meter");

                    if(distance <= 100) {
                        isNowOpened = true;

                        Optional<Member> oMember = memberRepository.findById(memoryReq.getMemberId());
                        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

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
                    .isMemoryMine(isMemoryMine)
                    .build());
        }

        MemoryRes memoryRes = MemoryRes.builder()
                .capsuleTitle(capsule.getTitle())
                .isGroup(capsule.isGroup())
                .rangeType(capsule.getRangeType())
                .address(capsule.getAddress())
                .isFirstGroup(isFirstGroup)
                .isCapsuleMine(isCapsuleMine)
                .memoryDetailDtoList(memoryDetailDtoList)
                .build();

        return new SuccessRes<>(true, "해당 캡슐의 추억을 조회합니다.", memoryRes);
    }

    @Override
    public CommonRes deleteMemory(int memoryId) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryId);
        Memory memory = oMemory.orElseThrow(() -> new CustomException(ErrorCode.MEMORY_NOT_FOUND));

        memory.getCommentList().forEach(comment -> commentRepository.save(Comment.of(comment, true)));
        memoryRepository.save(Memory.of(memory, true));

        return new CommonRes(true, "추억 삭제를 완료했습니다.");
    }

    @Override
    public SuccessRes<ModifyMemoryRes> getModifyMemoryInfo(int memoryId) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryId);
        Memory memory = oMemory.orElseThrow(() -> new CustomException(ErrorCode.MEMORY_NOT_FOUND));

        ModifyMemoryRes modifyMemoryRes = ModifyMemoryRes.builder()
                .memoryId(memoryId)
                .title(memory.getTitle())
                .content(memory.getContent())
                .imageUrl(memory.getImage().split("#"))
                .build();

        return new SuccessRes<>(true, "수정할 추억의 정보를 조회합니다.", modifyMemoryRes);
    }

    @Override
    public CommonRes modifyMemory(List<MultipartFile> multipartFileList, MemoryModifyReq memoryModifyReq) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryModifyReq.getMemoryId());
        Memory memory = oMemory.orElseThrow(() -> new CustomException(ErrorCode.MEMORY_NOT_FOUND));

        String image = memory.getImage();
        if(!multipartFileList.get(0).isEmpty()) image = awsS3Service.uploadFile(multipartFileList);

        memoryRepository.save(Memory.of(memory, memoryModifyReq.getTitle(), memoryModifyReq.getContent(), image));
        return new CommonRes(true, "추억 수정을 완료했습니다.");
    }

    @Override
    public CommonRes registComment(CommentRegistReq commentRegistReq) {
        Optional<Member> oMember = memberRepository.findById(commentRegistReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Optional<Memory> oMemory = memoryRepository.findById(commentRegistReq.getMemoryId());
        Memory memory = oMemory.orElseThrow(() -> new CustomException(ErrorCode.MEMORY_NOT_FOUND));

        commentRepository.save(Comment.builder()
                .member(member)
                .memory(memory)
                .content(commentRegistReq.getContent())
                .isDeleted(false)
                .build());

        fcmService.commentNotification(memory, member);

        return new CommonRes(true, "댓글 등록을 완료했습니다.");
    }

    @Override
    public SuccessRes<List<CommentRes>> getComment(int memoryId) {
        Optional<Memory> oMemory = memoryRepository.findById(memoryId);
        Memory memory = oMemory.orElseThrow(() -> new CustomException(ErrorCode.MEMORY_NOT_FOUND));

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
    public CommonRes setGroupFirstOpenDate(GroupOpenDateReq groupOpenDateReq) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(groupOpenDateReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND));

        boolean isLocked = (groupOpenDateReq.getOpenDate() != null && LocalDate.now().isBefore(groupOpenDateReq.getOpenDate()));
        capsule.getMemoryList().forEach(memory -> memoryRepository.save(Memory.of(memory, groupOpenDateReq.getOpenDate(), isLocked)));

        return new CommonRes(true, "그룹 캡슐의 최초 오픈 날짜 설정을 완료했습니다.");
    }
}
