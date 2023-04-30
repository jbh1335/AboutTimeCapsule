package com.example.oauthservice.service;

import com.example.oauthservice.db.entity.Member;
import com.example.oauthservice.db.repository.MemberRepository;
import com.example.oauthservice.security.OAuthAttributes;
import com.example.oauthservice.security.UserPrincipal;
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
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        return UserPrincipal.of(member,attributes.getAttributes());

    }

    private Member saveOrUpdate(OAuthAttributes attributes) {

        Optional<Member> userOptional = memberRepository.findByEmail(attributes.getEmail());
        Member member;
        if (userOptional.isPresent()){ // update
            member = userOptional.get();
            member.updateBySocialProfile(attributes.getProfileImageUrl());
        }
        else{ // save
            member = memberRepository.save(attributes.toMemberEntity());
        }

        return member;
    }
}