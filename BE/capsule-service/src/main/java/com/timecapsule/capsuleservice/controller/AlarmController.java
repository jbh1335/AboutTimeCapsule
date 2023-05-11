package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.request.AlarmTokenReq;
import com.timecapsule.capsuleservice.api.response.AlarmRes;
import com.timecapsule.capsuleservice.api.response.CommonRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    @GetMapping("list/{memberId}")
    public SuccessRes<List<AlarmRes>> getAlarmList(@PathVariable("memberId") int memberId) {
        return alarmService.getAlarmList(memberId);
    }

    @PatchMapping("/token/regist")
    public CommonRes saveAlarmToken(@RequestBody AlarmTokenReq alarmTokenReq) {
        return alarmService.saveAlarmToken(alarmTokenReq);
    }

    @PatchMapping("/token/delete/{memberId}")
    public CommonRes deleteAlarmToken(@PathVariable("memberId") int memberId) {
        return alarmService.deleteAlarmToken(memberId);
    }
}
