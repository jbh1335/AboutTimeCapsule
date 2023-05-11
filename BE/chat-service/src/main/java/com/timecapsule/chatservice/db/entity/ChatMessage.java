package com.timecapsule.chatservice.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timecapsule.chatservice.dto.MessageType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 921452535326804170L;
    
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member sender;
    private String content;
    private LocalDateTime createdDate;
}
