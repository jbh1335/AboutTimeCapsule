package com.example.oauthservice.security;

import com.example.oauthservice.db.entity.Member;
import com.example.oauthservice.db.entity.ProviderType;
import com.example.oauthservice.db.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {

    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String profileImageUrl;
    private RoleType roleType;
    private ProviderType providerType;


    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String,Object> attributes){

        switch (registrationId){
            case "naver":
                return ofNaver("id",attributes);
            case "kakao":
                return ofKakao("id",attributes);
            default:
                return null;
//                throw new NotSupportRegistrationIdException();
        }
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String,Object> attributes) {
        Map<String,Object> response = (Map<String,Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .profileImageUrl((String) attributes.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .providerType(ProviderType.NAVER)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String,Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");


        return OAuthAttributes.builder()
                .email((String) account.get("email"))
                .profileImageUrl((String) profile.get("profile_image_url"))
                .attributes(account)
                .nameAttributeKey(userNameAttributeName)
                .providerType(ProviderType.KAKAO)
                .build();
    }

    public Member toMemberEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .profileImageUrl(profileImageUrl)
                .roleType(roleType)
                .providerType(providerType)
                .build();
    }
}