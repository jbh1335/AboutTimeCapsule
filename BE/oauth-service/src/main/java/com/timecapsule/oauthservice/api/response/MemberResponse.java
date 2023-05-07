package com.timecapsule.oauthservice.api.response;

import com.timecapsule.oauthservice.db.entity.ProviderType;
import com.timecapsule.oauthservice.db.entity.RoleType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    int id;
    private String nickName;
    private String email;
    private String profileImageUrl;
    private RoleType roleType;
}
