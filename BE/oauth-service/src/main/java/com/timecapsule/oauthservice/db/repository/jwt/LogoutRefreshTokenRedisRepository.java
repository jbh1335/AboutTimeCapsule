package com.timecapsule.oauthservice.db.repository.jwt;

import com.timecapsule.oauthservice.db.entity.jwt.LogoutRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutRefreshTokenRedisRepository extends CrudRepository<LogoutRefreshToken, String> {
}