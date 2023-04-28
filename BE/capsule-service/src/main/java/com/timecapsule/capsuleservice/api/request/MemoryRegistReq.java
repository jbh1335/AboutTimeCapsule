package com.timecapsule.capsuleservice.api.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
public class MemoryRegistReq {
    private int memberId;
    private int capsuleId;
    private String title;
    private String content;
    private Date openDate;
}
