package com.timecapsule.oauthservice.security;

import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.entity.RoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
// 인증된 유저를 저장
// Security 에서 인증 객체로 사용할 클래스
// OAuth2.0 으로 로그인 할 시 OAuth2User에서 해당 유저의 정보들을 획득
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
    // 서버에서 access token 을 얻은 다음 이 token 으로 Provider한테 사용자 정보를 요청하면 attributes로 응답됨
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.toString()));
    }

    @Override
    public String getName() {
        return member.getName();
    }


    public String getUserName() {
        return member.getEmail();
    }
}