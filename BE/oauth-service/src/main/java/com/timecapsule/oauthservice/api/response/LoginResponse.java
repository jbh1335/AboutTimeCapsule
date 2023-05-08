package com.timecapsule.oauthservice.api.response;

import com.timecapsule.oauthservice.db.entity.RoleType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    int id;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private RoleType roleType;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
}