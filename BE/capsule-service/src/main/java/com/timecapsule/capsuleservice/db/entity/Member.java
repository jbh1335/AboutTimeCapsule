package com.timecapsule.capsuleservice.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity{
    @Column(length = 64)
    private String name;
    @Column(length = 64, unique = true)
    private String nickname;
    @Column(length = 64, unique = true)
    private String email;
    @Column(length = 255)
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    @OneToMany(mappedBy = "fromMember")
    private List<Friend> fromMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "toMember")
    private List<Friend> toMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<CapsuleMember> capsuleMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<CapsuleOpenMember> capsuleOpenMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Memory> memoryList = new ArrayList<>();

}
