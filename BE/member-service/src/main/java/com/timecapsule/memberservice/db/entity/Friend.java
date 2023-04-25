package com.timecapsule.memberservice.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Getter
@NoArgsConstructor
@Entity
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
