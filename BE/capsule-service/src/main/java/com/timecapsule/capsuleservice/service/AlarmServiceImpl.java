package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.response.AlarmRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.db.entity.Alarm;
import com.timecapsule.capsuleservice.db.entity.Memory;
import com.timecapsule.capsuleservice.db.repository.AlarmRepository;
import com.timecapsule.capsuleservice.db.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("alarmService")
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService{
    private final AlarmRepository alarmRepository;

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

        return new SuccessRes<>(true, "알림 내역을 조회합니다.", alarmResList);
    }
}
