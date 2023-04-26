package com.timecapsule.memberservice.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Capsule extends BaseEntity {
    @Column(length = 128)
    private String title;
    private Date openDate;
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private Range range;
    private boolean isLocked;
    private boolean isGroup;
    @OneToMany(mappedBy = "capsule")
    private List<Memory> memoryList = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private List<CapsuleMember> capsuleMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private List<CapsuleOpenMember> capsuleOpenMemberList = new ArrayList<>();
    @OneToOne(mappedBy = "capsule")
    private Place place;

}
