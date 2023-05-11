package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.LoginRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;
import com.timecapsule.oauthservice.api.response.OauthTokenRes;

public interface OauthService {
    SuccessRes<LoginRes> login(String providerName, String token);
    SuccessRes<OauthTokenRes> getOauthToken(String providerName, String code);
}