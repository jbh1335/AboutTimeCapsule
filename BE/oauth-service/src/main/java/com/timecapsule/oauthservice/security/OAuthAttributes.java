package com.timecapsule.oauthservice.security;

import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.db.entity.ProviderType;
import com.timecapsule.oauthservice.db.entity.RoleType;
import com.timecapsule.oauthservice.exception.NotSupportRegistrationIdException;
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
    private String oauthId;
    private String email;
    private String profileImageUrl;
    private RoleType roleType;
    private ProviderType providerType;


    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String,Object> attributes){

        switch (registrationId){
            case "naver":
                return ofNaver(userNameAttributeName,attributes);
            case "kakao":
                return ofKakao(userNameAttributeName,attributes);
            default:
                throw new NotSupportRegistrationIdException();
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
                .roleType(RoleType.USER)
                .oauthId((String) response.get(userNameAttributeName))
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
                .roleType(RoleType.USER)
                .oauthId((String) account.get(userNameAttributeName))
                .build();
    }

    public Member toMemberEntity(){
        return Member.builder()
                .email(email)
                .oauthId(oauthId)
                .profileImageUrl(profileImageUrl)
                .roleType(roleType.USER)
                .providerType(providerType)
                .build();
    }
}