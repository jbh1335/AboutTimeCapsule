package com.example.oauthservice.db.repository.jwt;

import com.example.oauthservice.db.entity.jwt.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}