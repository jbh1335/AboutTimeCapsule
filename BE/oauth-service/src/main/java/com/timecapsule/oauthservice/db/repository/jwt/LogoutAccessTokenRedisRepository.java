package com.timecapsule.oauthservice.db.repository.jwt;

import com.timecapsule.oauthservice.db.entity.jwt.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}