package com.timecapsule.memberservice.db.entity;

import javax.persistence.*;

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
