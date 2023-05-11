package com.timecapsule.memberservice.service;

import com.timecapsule.memberservice.dto.MessageDto;

public interface FcmService {
    void sendMessage(MessageDto messageDto);
}
