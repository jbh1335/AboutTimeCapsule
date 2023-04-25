package com.timecapsule.capsuleservice.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class Friend extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "from_member_id", insertable = false, updatable = false)
    private Member fromMemberId;

    @ManyToOne
    @JoinColumn(name = "to_member_id", insertable = false, updatable = false)
    private Member toMemberId;

    private boolean isAccepted;
}
