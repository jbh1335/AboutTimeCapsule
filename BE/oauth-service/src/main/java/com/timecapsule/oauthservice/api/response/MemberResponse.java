package com.timecapsule.oauthservice.api.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    private String nickname;
    private String email;
    private String profileImageUrl;
}
