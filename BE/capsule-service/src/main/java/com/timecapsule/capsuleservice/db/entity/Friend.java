package com.timecapsule.capsuleservice.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
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
