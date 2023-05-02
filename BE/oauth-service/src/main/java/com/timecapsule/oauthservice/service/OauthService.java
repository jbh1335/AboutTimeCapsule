package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.dto.AuthResponseDto;
import org.springframework.stereotype.Service;

public interface OauthService {
    void logout(String accessToken, String refreshToken);
    AuthResponseDto reissue(String refreshToken);
}
