package com.timecapsule.chatservice.db.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chatroom extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;
}