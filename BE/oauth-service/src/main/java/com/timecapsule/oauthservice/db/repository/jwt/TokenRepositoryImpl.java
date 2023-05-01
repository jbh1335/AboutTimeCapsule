package com.timecapsule.oauthservice.db.repository.jwt;

import com.timecapsule.oauthservice.db.entity.jwt.LogoutAccessToken;
import com.timecapsule.oauthservice.db.entity.jwt.LogoutRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final LogoutRefreshTokenRedisRepository logoutRefreshTokenRedisRepository;

    @Override
    public void saveLogoutAccessToken(LogoutAccessToken logoutAccessToken) {
        logoutAccessTokenRedisRepository.save(logoutAccessToken);
    }

    @Override
    public void saveLogoutRefreshToken(LogoutRefreshToken logoutRefreshToken) {
        logoutRefreshTokenRedisRepository.save(logoutRefreshToken);
    }

    @Override
    public boolean existsLogoutAccessTokenById(String token) {
        return logoutAccessTokenRedisRepository.existsById(token);
    }

    @Override
    public boolean existsLogoutRefreshTokenById(String token) {
        return logoutRefreshTokenRedisRepository.existsById(token);
    }
}