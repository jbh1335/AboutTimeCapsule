package com.timecapsule.memberservice.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Alarm extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member memberId;
    @Column(length = 255)
    private String content;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int capsuleId;
}
