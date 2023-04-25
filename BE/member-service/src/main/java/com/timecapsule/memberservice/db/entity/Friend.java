package com.timecapsule.memberservice.db.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Friend extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member fromMemberId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member toMemberId;

    @Column(columnDefinition = "TINYINT", length=1)
    private int isAccepted;
}
