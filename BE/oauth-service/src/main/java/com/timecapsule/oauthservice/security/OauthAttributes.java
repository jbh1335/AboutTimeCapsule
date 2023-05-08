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
        return Arrays.stream(values()) // values()를 통하여 로그인한 사용자가 가지고 있는 권한들을 스트림으로 변화(values()는 enum의 요소들을 순서대로 배열에 리턴)
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new) // 입력받은 매개변수 providerName 일치하지 않는 경우, 해당 서비스는 올바르지 않은 서비스라고 판단하여 오류 반환
                .of(userAttributes); // 정보가 일치한다면 userAttributes 반환
    }

    public abstract Member of(Map<String, Object> attributes);
}