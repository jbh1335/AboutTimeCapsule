package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.CapsuleRegistReq;
import com.timecapsule.capsuleservice.api.request.MemoryRegistReq;
import com.timecapsule.capsuleservice.api.response.CapsuleListRes;
import com.timecapsule.capsuleservice.api.response.OpenedCapsuleListRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.db.entity.*;
import com.timecapsule.capsuleservice.db.repository.*;
import com.timecapsule.capsuleservice.dto.MapInfoDto;
import com.timecapsule.capsuleservice.dto.OpenedCapsuleDto;
import com.timecapsule.capsuleservice.dto.UnopenedCapsuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service("capsuleService")
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    private final AwsS3Service awsS3Service;
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final MemoryOpenMemberRepository memoryOpenMemberRepository;
    private final MemoryRepository memoryRepository;

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

        return new SuccessRes<Integer>(true, "캡슐 등록을 완료했습니다.", newCapsule.getId());
    }

    @Override
    public SuccessRes<Integer> registMemory(List<MultipartFile> multipartFileList, MemoryRegistReq memoryRegistReq) {
        Optional<Capsule> oCapsule = capsuleRepository.findById(memoryRegistReq.getCapsuleId());
        Capsule capsule = oCapsule.orElseThrow(() -> new IllegalArgumentException("capsule doesn't exist"));

        Optional<Member> oMember = memberRepository.findById(memoryRegistReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        String image = awsS3Service.uploadFile(multipartFileList);

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

        return new SuccessRes<Integer>(true, "추억 등록을 완료했습니다.", memoryId);
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

        Collections.sort(unopenedCapsuleDtoList, (o1, o2) -> o1.getOpenDate().compareTo(o2.getOpenDate()));
        Collections.sort(openedCapsuleDtoList, (o1, o2) -> o1.getOpenDate().compareTo(o2.getOpenDate()));

        CapsuleListRes capsuleListRes = CapsuleListRes.builder()
                .unopenedCapsuleDtoList(unopenedCapsuleDtoList)
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();

        return new SuccessRes<CapsuleListRes>(true, "나의 캡슐 목록을 조회합니다.", capsuleListRes);
    }

    @Override
    public SuccessRes<OpenedCapsuleListRes> getOpenCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();
        List<MapInfoDto> mapInfoDtoList = new ArrayList<>();

        for(MemoryOpenMember memoryOpenMember : member.getMemoryOpenMemberList()) {
            Capsule capsule = memoryOpenMember.getCapsule();
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

        OpenedCapsuleListRes openedCapsuleListRes = OpenedCapsuleListRes.builder()
                .openedCapsuleDtoList(openedCapsuleDtoList)
                .mapInfoDtoList(mapInfoDtoList)
                .build();
        return new SuccessRes<OpenedCapsuleListRes>(true, "나의 방문 기록을 조회합니다.", openedCapsuleListRes);
    }
}
