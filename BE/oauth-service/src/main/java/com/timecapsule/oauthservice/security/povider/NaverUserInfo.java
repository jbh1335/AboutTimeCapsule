package com.timecapsule.oauthservice.security.povider;

import com.timecapsule.oauthservice.db.entity.ProviderType;

import java.util.Map;

public class NaverUserInfo implements OauthUserInfo {
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String) getResponse().get("id");
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.NAVER;
    }

    @Override
    public String getEmail() {
        return (String) getResponse().get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) getResponse().get("profile_image");
    }

    public Map<String, Object> getResponse(){
        return (Map<String, Object>) attributes.get("response");
    }
}
