package com.timecapsule.oauthservice.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity{
    @Column(length = 64)
    private String nickName;
    @Column(length = 64)
    private String email;
    @Column(length = 255)
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType; // 요청이 들어온 서드 파티 앱
    private String providerId; // 서드 파티 앱의 PK
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @OneToMany(mappedBy = "fromMember")
    private List<Friend> fromMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "toMember")
    private List<Friend> toMemberList = new ArrayList<>();

    public void updateProfile(String email, String profileImageUrl) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
