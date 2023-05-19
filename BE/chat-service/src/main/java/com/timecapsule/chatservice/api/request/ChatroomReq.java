package com.timecapsule.chatservice.api.request;

import lombok.Getter;

@Getter
public class ChatroomReq {
    private Integer fromMemberId;
    private Integer toMemberId;
}
