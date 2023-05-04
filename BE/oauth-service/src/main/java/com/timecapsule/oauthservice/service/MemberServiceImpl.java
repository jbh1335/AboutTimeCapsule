package com.timecapsule.oauthservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MemberServiceImpl implements MemberService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    @Override
    public boolean isExistUsername(String username) {
        return false;
    }


}
