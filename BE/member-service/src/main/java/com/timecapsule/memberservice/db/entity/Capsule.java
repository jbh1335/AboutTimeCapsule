package com.timecapsule.memberservice.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Capsule extends BaseEntity {
    @Column(length = 128)
    private String title;
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private RangeType rangeType;
    private boolean isGroup;
    private Double latitude;
    private Double longitude;
    @Column(length = 128)
    private String address;
    @OneToMany(mappedBy = "capsule")
    private List<Memory> memoryList = new ArrayList<>();
    @OneToMany(mappedBy = "capsule")
    private List<CapsuleMember> capsuleMemberList = new ArrayList<>();

    @Builder
    public Capsule(String title, RangeType rangeType, boolean isGroup, Double latitude, Double longitude, String address) {
        this.title = title;
        this.rangeType = rangeType;
        this.isGroup = isGroup;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public static Capsule of(Capsule capsule, boolean isDeleted) {
        capsule.setDeleted(isDeleted);
        return capsule;
    }

    public static Capsule of(Capsule capsule, RangeType rangeType) {
        capsule.setRangeType(rangeType);
        return capsule;
    }
}
