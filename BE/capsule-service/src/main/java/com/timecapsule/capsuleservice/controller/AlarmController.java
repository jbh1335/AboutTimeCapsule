package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.response.AlarmRes;
import com.timecapsule.capsuleservice.api.response.SuccessRes;
import com.timecapsule.capsuleservice.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
