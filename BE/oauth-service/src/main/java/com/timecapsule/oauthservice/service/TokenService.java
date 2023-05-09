package com.timecapsule.oauthservice.service;

import com.timecapsule.oauthservice.api.response.AccessTokenRes;
import com.timecapsule.oauthservice.api.request.RefreshTokenReq;
import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.api.response.SuccessRes;

public interface TokenService {
    int findMemberByToken(String accessToken);
    SuccessRes<AccessTokenRes> accessTokenByRefreshToken(String accessToken, RefreshTokenReq refreshTokenReq);
    CommonRes logout(String accessToken);
}