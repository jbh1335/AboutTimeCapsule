package com.timecapsule.chatservice.api.request;

import com.timecapsule.chatservice.dto.MessageType;
import lombok.Getter;

@Getter
public class MessageReq {
    private MessageType type;
    private String chatroomId;
    private Integer sender;
    private String content;
}
