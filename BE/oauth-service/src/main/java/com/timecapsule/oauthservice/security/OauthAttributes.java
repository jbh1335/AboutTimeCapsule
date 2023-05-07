package com.timecapsule.oauthservice.security;

import com.timecapsule.oauthservice.db.entity.Member;
import com.timecapsule.oauthservice.security.povider.KakaoUserInfo;
import com.timecapsule.oauthservice.security.povider.NaverUserInfo;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {
    KAKAO("kakao") {
        @Override
        public Member of(Map<String, Object> attributes) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
            return Member.builder()
                    .email(kakaoUserInfo.getEmail())
                    .providerType(kakaoUserInfo.getProvider())
                    .providerId(kakaoUserInfo.getProviderId())
                    .profileImageUrl(kakaoUserInfo.getImageUrl())
                    .build();
        }
    },

    NAVER("naver") {
        @Override
        public Member of(Map<String, Object> attributes) {
            NaverUserInfo naverUserInfo = new NaverUserInfo(attributes);
            return Member.builder()
                    .email(naverUserInfo.getEmail())
                    .providerType(naverUserInfo.getProvider())
                    .providerId(naverUserInfo.getProviderId())
                    .profileImageUrl(naverUserInfo.getImageUrl())
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(String providerName) {
        this.providerName = providerName;
    }

    public static Member extract(String providerName, Map<String, Object> userAttributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(userAttributes);
    }

    public abstract Member of(Map<String, Object> attributes);
}