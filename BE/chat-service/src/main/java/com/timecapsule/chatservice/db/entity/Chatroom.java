package com.timecapsule.chatservice.db.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chatroom extends BaseEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;
}