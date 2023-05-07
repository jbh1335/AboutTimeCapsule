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
    private String nickName;
    private String email;
    private String profileImageUrl;
}
