package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.api.request.AlarmTokenReq;
import com.timecapsule.capsuleservice.api.response.AlarmRes;
import com.timecapsule.capsuleservice.api.response.CommonRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;

import java.util.List;

public interface AlarmService {
    SuccessRes<List<AlarmRes>> getAlarmList(int memberId);
    CommonRes saveAlarmToken(AlarmTokenReq alarmTokenReq);
    CommonRes deleteAlarmToken(int memberId);
}
