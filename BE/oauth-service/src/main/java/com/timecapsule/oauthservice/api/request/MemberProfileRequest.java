package com.timecapsule.oauthservice.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberProfileRequest {
    private String nickname;
    private String email;
    private String profileImageUrl;
}
