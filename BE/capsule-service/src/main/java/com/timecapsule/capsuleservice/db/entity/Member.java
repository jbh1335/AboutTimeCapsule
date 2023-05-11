package com.timecapsule.capsuleservice.db.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Column(length = 64)
    private String nickname;
    @Column(length = 64)
    private String email;
    @Column(length = 255)
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType; // 요청이 들어온 서드 파티 앱
    private String providerId; // 서드 파티 앱의 PK
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoleType roleType = RoleType.USER;
    private String alarmToken;
    @OneToMany(mappedBy = "fromMember")
    private List<Friend> fromMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "toMember")
    private List<Friend> toMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<CapsuleMember> capsuleMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<MemoryOpenMember> memoryOpenMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Memory> memoryList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Alarm> alarmList = new ArrayList<>();

    public static Member of(Member member, String alarmToken) {
        member.setAlarmToken(alarmToken);
        return member;
    }
}
