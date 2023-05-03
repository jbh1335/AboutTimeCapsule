package com.timecapsule.oauthservice.api.response;

import com.timecapsule.oauthservice.dto.AuthResponseDto;
import lombok.Getter;

@Getter
//@NoArgsConstructor
public class TokenResponse {
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;

    public static TokenResponse from (AuthResponseDto authResponseDto) {
        return new TokenResponse(authResponseDto.getAccessToken(), authResponseDto.getRefreshToken());
    }

    public static TokenResponse of(String accessToken, String refreshToken){
        return new TokenResponse(accessToken,refreshToken);
    }

    private TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}