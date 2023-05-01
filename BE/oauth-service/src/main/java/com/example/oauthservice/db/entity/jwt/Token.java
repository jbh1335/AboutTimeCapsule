package com.example.oauthservice.db.entity.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public abstract class Token {

    @Id
    private String id;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;
}
