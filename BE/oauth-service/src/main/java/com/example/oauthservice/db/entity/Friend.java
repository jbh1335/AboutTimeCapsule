package com.example.oauthservice.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class Friend extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", insertable = false, updatable = false)
    private Member fromMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", insertable = false, updatable = false)
    private Member toMember;
    private boolean isAccepted;
}

