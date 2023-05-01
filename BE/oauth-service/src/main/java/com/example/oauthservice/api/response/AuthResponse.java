package com.example.oauthservice.api.response;

import com.example.oauthservice.dto.AuthResponseDto;
import lombok.Getter;

@Getter
//@NoArgsConstructor
public class AuthResponse {
    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;

    public static AuthResponse from (AuthResponseDto authResponseDto) {
        return new AuthResponse(authResponseDto.getAccessToken(), authResponseDto.getRefreshToken());
    }

    public static AuthResponse of(String accessToken,String refreshToken){
        return new AuthResponse(accessToken,refreshToken);
    }

    private AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}