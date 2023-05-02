package com.timecapsule.oauthservice.controller;

import com.timecapsule.oauthservice.api.response.AuthResponse;
import com.timecapsule.oauthservice.dto.AuthResponseDto;
import com.timecapsule.oauthservice.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@PreAuthorize("isAuthenticated()") // 메서드 실행 전 (요청전)에 권한 확인 -> 현재 사용자가 로그인 상태라면 true
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization") String accessToken,
                                       @RequestHeader(value = "refreshToken") String refreshToken) {
        oauthService.logout(accessToken,refreshToken);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/reissue") // 재발급
    public ResponseEntity<AuthResponse> reissueToken(@RequestHeader(value = "Authorization") String refreshToken){
        AuthResponseDto authResponseDto = oauthService.reissue(refreshToken);
        return ResponseEntity.ok(AuthResponse.from(authResponseDto));
    }
}
