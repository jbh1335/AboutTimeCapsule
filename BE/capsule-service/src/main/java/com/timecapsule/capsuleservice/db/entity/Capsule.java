package com.timecapsule.capsuleservice.db.entity;

import lombok.Builder;
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
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private RangeType rangeType;
    private boolean isLocked;
    private boolean isGroup;
    private Double latitude;
    private Double longitude;
    @Column(length = 128)
    private String address;
    @OneToMany(mappedBy = "capsule")
    private List<Memory> memoryList = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private List<CapsuleMember> capsuleMemberList = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private List<CapsuleOpenMember> capsuleOpenMemberList = new ArrayList<>();

    @Builder
    public Capsule(String title, boolean isDeleted, RangeType rangeType, boolean isLocked, boolean isGroup, Double latitude, Double longitude, String address) {
        this.title = title;
        this.isDeleted = isDeleted;
        this.rangeType = rangeType;
        this.isLocked = isLocked;
        this.isGroup = isGroup;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
