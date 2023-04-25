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
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatRoom;
    private int MemberId;
    private String content;
    private String createdDate;
}
