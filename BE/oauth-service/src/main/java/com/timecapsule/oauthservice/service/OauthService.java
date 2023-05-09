package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.request.AuthorizationRequest;
import com.timecapsule.oauthservice.api.response.LoginResponse;

public interface OauthService {
    LoginResponse login(AuthorizationRequest authorizationRequest);
}