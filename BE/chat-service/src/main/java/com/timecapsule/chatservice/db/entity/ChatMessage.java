package com.timecapsule.chatservice.db.entity;

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
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 921452535326804170L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;
    private Integer sender;
    private String content;
    private LocalDateTime createdDate;
}
