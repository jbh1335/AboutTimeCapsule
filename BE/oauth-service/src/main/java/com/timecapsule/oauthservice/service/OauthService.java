package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.dto.AuthorizationReqDto;
import com.timecapsule.oauthservice.api.response.LoginRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;

public interface OauthService {
    SuccessRes<LoginRes> login(AuthorizationReqDto authorizationReqDto);
}