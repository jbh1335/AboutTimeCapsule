package com.timecapsule.oauthservice.util;

import com.timecapsule.oauthservice.db.entity.RoleType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserAuthentication extends AbstractAuthenticationToken {

    private final int memberId;

    public UserAuthentication(int memberId) {
        super(authorities());
        this.memberId = memberId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return memberId;
    }

    public int getMemberId() {
        return memberId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    private static List<GrantedAuthority> authorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleType.USER.toString()));
        return authorities;
    }
}
