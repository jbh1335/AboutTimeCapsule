package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.CapsuleRegistReq;
import com.timecapsule.capsuleservice.api.response.CapsuleListRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.db.entity.Capsule;
import com.timecapsule.capsuleservice.db.entity.CapsuleMember;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.repository.CapsuleMemberRepository;
import com.timecapsule.capsuleservice.db.repository.CapsuleOpenMemberRepository;
import com.timecapsule.capsuleservice.db.repository.CapsuleRepository;
import com.timecapsule.capsuleservice.db.repository.MemberRepository;
import com.timecapsule.capsuleservice.dto.OpenedCapsuleDto;
import com.timecapsule.capsuleservice.dto.UnopenedCapsuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("capsuleService")
@RequiredArgsConstructor
public class CapsuleServiceImpl implements CapsuleService {
    private final CapsuleRepository capsuleRepository;
    private final MemberRepository memberRepository;
    private final CapsuleMemberRepository capsuleMemberRepository;
    private final CapsuleOpenMemberRepository capsuleOpenMemberRepository;

    @Override
    public SuccessRes<Integer> registCapsule(CapsuleRegistReq capsuleRegistReq) {
        Capsule capsule = Capsule.builder()
                .title(capsuleRegistReq.getTitle())
                .isDeleted(false)
                .rangeType(capsuleRegistReq.getRangeType())
                .isLocked(true)
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
    public SuccessRes<CapsuleListRes> getMyCapsule(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new IllegalArgumentException("member doesn't exist"));

        List<UnopenedCapsuleDto> unopenedCapsuleDtoList = new ArrayList<>();
        List<OpenedCapsuleDto> openedCapsuleDtoList = new ArrayList<>();

        for(CapsuleMember capsuleMember : member.getCapsuleMemberList()) {
            Capsule capsule = capsuleMember.getCapsule();

            boolean isExisted = capsuleOpenMemberRepository.existsByCapsuleIdAndMemberId(capsule.getId(), memberId);

            // 잠겨O 열람 O -> 불가능
            // 잠겨O 열람 X -> 위에

            // 잠겨X 열람 O -> 밑에
            // 잠겨X 열람 X -> 위에
            if(!isExisted && capsule.isLocked()) {
                unopenedCapsuleDtoList.add(UnopenedCapsuleDto.builder()
                        .capsuleId(capsule.getId()) // openDate 추가
                        .address(capsule.getAddress())
                        .isLocked(true)
                        .build());
            } else {

                // 열람
                if(isExisted) {

                } else {

                }

                // 미열람

            }

        }
        return null;
    }
}
