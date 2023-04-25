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
public class Chatroom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "from_member_id", insertable=false, updatable=false)
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "to_member_id", insertable=false, updatable=false)
    private Member toMember;
}
