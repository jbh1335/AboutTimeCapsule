package com.timecapsule.oauthservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    boolean isExistUsername (String username);
}