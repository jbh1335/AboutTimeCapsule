package com.timecapsule.oauthservice.db.repository.jwt;

import com.timecapsule.oauthservice.db.entity.jwt.LogoutAccessToken;
import com.timecapsule.oauthservice.db.entity.jwt.LogoutRefreshToken;

public interface TokenRepository {

    void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken);

    void saveLogoutRefreshToken(LogoutRefreshToken logoutRefreshToken);

    boolean existsLogoutAccessTokenById(String token);

    boolean existsLogoutRefreshTokenById(String token);
}
