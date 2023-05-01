package com.timecapsule.oauthservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@PreAuthorize("isAuthenticated()") // 메서드 실행 전 (요청전)에 권한 확인
@RequiredArgsConstructor
public class OauthController {
}
