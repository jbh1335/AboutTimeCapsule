package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;

@Getter
public class CommentRegistReq {
    private int memoryId;
    private int memberId;
    private String content;
}
