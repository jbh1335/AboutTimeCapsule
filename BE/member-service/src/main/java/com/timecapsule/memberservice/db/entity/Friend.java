package com.timecapsule.memberservice.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Friend extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;
    private boolean isAccepted;

    @Builder
    public Friend(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

    public static Friend of(Friend friend, boolean isAccepted) {
        friend.setAccepted(isAccepted);
        return friend;
    }
}
