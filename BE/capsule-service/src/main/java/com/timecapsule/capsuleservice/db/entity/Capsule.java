package com.timecapsule.capsuleservice.db.entity;

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
    @OneToMany(mappedBy = "Capsule")
    private List<Memory> memoryList = new ArrayList<>();
    @OneToMany(mappedBy = "Capsule")
    private List<CapsuleMember> capsuleMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "Capsule")
    private List<CapsuleOpenMember> capsuleOpenMemberList = new ArrayList<>();
    @OneToOne(mappedBy = "Capsule")
    private Place place;

}
