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
    private static final long serialVersionUID = -7401006532253358108L;

    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "from_member_id", insertable=false, updatable=false)
    private Member fromMember;
    @ManyToOne
    @JoinColumn(name = "to_member_id", insertable=false, updatable=false)
    private Member toMember;
}