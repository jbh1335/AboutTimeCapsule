package com.timecapsule.oauthservice.dto;

import lombok.Setter;

@Setter
public class MemberDto {
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String oauthId;
}
