package com.example.oauthservice.db.repository.jwt;

import com.example.oauthservice.db.entity.jwt.LogoutRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutRefreshTokenRedisRepository extends CrudRepository<LogoutRefreshToken, String> {
}