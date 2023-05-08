package com.timecapsule.oauthservice.security.povider;

import com.timecapsule.oauthservice.db.entity.ProviderType;

public interface OauthUserInfo {
    String getProviderId();
    ProviderType getProvider();
    String getEmail();
    String getImageUrl();
}