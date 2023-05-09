package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.dto.RequestDto;
import com.timecapsule.capsuleservice.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDTO) throws IOException {
        System.out.println("controller");
        System.out.println(requestDTO.getTargetToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());

        fcmService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }
}
