package com.timecapsule.oauthservice.controller;

import com.timecapsule.oauthservice.api.response.LoginResponse;
import com.timecapsule.oauthservice.api.response.TokenResponse;
import com.timecapsule.oauthservice.dto.AuthResponseDto;
import com.timecapsule.oauthservice.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/oauth2")
//@PreAuthorize("isAuthenticated()") // 메서드 실행 전 (요청전)에 권한 확인 -> 현재 사용자가 로그인 상태라면 true
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/callback/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code) {
        log.info(provider + " " + code);
//        LoginResponse loginResponse = oauthService.login(provider, code);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization") String accessToken,
                                       @RequestHeader(value = "refreshToken") String refreshToken) {
        oauthService.logout(accessToken,refreshToken);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/reissue") // 재발급
    public ResponseEntity<TokenResponse> reissueToken(@RequestHeader(value = "Authorization") String refreshToken){
        AuthResponseDto authResponseDto = oauthService.reissue(refreshToken);
        return ResponseEntity.ok(TokenResponse.from(authResponseDto));
    }


}
