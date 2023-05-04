package com.timecapsule.oauthservice.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{
    @Column(length = 64)
    private String nickname;
    @Column(length = 64)
    private String email;
    @Column(length = 255)
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType; // 요청이 들어온 서드 파티 앱
    private String oauthId; // 서드 파티 앱의 PK
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @OneToMany(mappedBy = "fromMember")
    private List<Friend> fromMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "toMember")
    private List<Friend> toMemberList = new ArrayList<>();

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
