package com.timecapsule.oauthservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 스프링 시큐리티에서 Authentication Manager를 사용하기 위해서는 UserDetailsService 를 구현 필수 => 안하면 에러남
@Service
public class UserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}