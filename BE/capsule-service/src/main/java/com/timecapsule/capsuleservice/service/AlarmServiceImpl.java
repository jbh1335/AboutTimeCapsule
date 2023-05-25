package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.AlarmTokenReq;
import com.timecapsule.capsuleservice.api.response.AlarmRes;
import com.timecapsule.capsuleservice.api.response.CommonRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.db.entity.Alarm;
import com.timecapsule.capsuleservice.db.entity.Member;
import com.timecapsule.capsuleservice.db.repository.AlarmRepository;
import com.timecapsule.capsuleservice.db.repository.MemberRepository;
import com.timecapsule.capsuleservice.exception.CustomException;
import com.timecapsule.capsuleservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("alarmService")
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService{
    private final RedisService redisService;
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Override
    public SuccessRes<List<AlarmRes>> getAlarmList(int memberId) {
        List<AlarmRes> alarmResList = new ArrayList<>();
        List<Alarm> alarmList = alarmRepository.findAllByMemberId(memberId);

        alarmList.forEach(alarm -> alarmResList.add(AlarmRes.builder()
                .alarmId(alarm.getId())
                .memberId(memberId)
                .capsuleId(alarm.getCapsuleId())
                .content(alarm.getContent())
                .categoryType(alarm.getCategoryType())
                .build()));

        redisService.setData("alarm_new", String.valueOf(memberId), false);
        return new SuccessRes<>(true, "알림 내역을 조회합니다.", alarmResList);
    }

    @Override
    public CommonRes saveAlarmToken(AlarmTokenReq alarmTokenReq) {
        Optional<Member> oMember = memberRepository.findById(alarmTokenReq.getMemberId());
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        redisService.setData("alarm", String.valueOf(member.getId()), alarmTokenReq.getToken());
        return new CommonRes(true, "알림 토큰을 저장했습니다.");
    }

    @Override
    public CommonRes deleteAlarmToken(int memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        redisService.deleteData("alarm", memberId);
        return new CommonRes(true, "알림 토큰을 삭제했습니다.");
    }
}
