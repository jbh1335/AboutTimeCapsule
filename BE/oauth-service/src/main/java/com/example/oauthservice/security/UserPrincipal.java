package com.example.oauthservice.security;

import com.example.oauthservice.db.entity.Member;
import com.example.oauthservice.db.entity.RoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
//  Security 에서 인증 객체로 사용할 클래스
public class UserPrincipal implements OAuth2User {
    private Member member;

    private Map<String, Object> attributes;

    private UserPrincipal(Member member) {
        this.member = member;
    }

    public static UserPrincipal from(Member member) {
        return new UserPrincipal(member);
    }

    public static UserPrincipal of(Member member, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.from(member);
        userPrincipal.attributes = attributes;
        return userPrincipal;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(RoleType.ADMIM.toString()));
    }

    @Override
    public String getName() {
        return member.getName();
    }


    public String getEmail() {
        return member.getEmail();
    }
}