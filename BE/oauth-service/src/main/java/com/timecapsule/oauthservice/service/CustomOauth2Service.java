package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.repository.MemberRepository;
import com.timecapsule.oauthservice.security.OAuthAttributes;
import com.timecapsule.oauthservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
// 인증된 저장된 유저를 불러오는 클래스
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    // accessToken까지 얻은다음 실행, oAuth2UserRequest에는 access token과 같은 정보들이 들어있음
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        OAuth2User oAuth2User = super.loadUser(userRequest); // access token을 이용해 Provider 서버로부터 사용자 정보를 받아옴

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        return UserPrincipal.of(member,attributes.getAttributes());

    }

    // 해당 사용자가 이미 회원가입 되어있는 사용자인지 확인한다.
    // 만약 회원가입이 되어있지 않다면, 회원가입 처리한다.
    // 만약 회원가입이 되어있다면, 프로필사진URL 등의 정보를 업데이트한다.
    private Member saveOrUpdate(OAuthAttributes attributes) {

        Optional<Member> userOptional = memberRepository.findByEmail(attributes.getEmail());
        Member member;
        if (userOptional.isPresent()){ // update
            member = userOptional.get();
            member.setProfileImageUrl(attributes.getProfileImageUrl());
            return memberRepository.save(member);
        }
        else{ // save
            return  memberRepository.save(attributes.toMemberEntity());
        }
    }
}