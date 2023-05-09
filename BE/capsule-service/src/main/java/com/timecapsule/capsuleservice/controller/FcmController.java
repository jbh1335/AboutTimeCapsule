package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.request.AlarmReq;
import com.timecapsule.capsuleservice.api.response.CommonRes;
import com.timecapsule.capsuleservice.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/push")
    public CommonRes pushMessage(@RequestBody AlarmReq alarmReq) throws IOException {
        System.out.println("controller");
        System.out.println(alarmReq.getTargetToken() + " "
                + alarmReq.getTitle() + " " + alarmReq.getBody());

        return fcmService.sendMessageTo(alarmReq.getTargetToken(), alarmReq.getTitle(), alarmReq.getBody());
    }
}
