package com.timecapsule.oauthservice.security.jwt;

import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.exception.ErrorCode;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

@Component
public class JwtTokenProvider {
    @Value("${jwt.access-expiration-time}")
    private long accessTokenValidityInMilliseconds;
    @Value("${jwt.refresh-expiration-time}")
    private long refreshTokenValidityInMilliseconds;
    @Value("${jwt.secret-key}")
    private String secretKey;

    public Token createAccessToken(String payload) {
        String token = createToken(payload,accessTokenValidityInMilliseconds);
        return new Token(token, accessTokenValidityInMilliseconds);
    }

    public Token createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        String token = createToken(generatedString, refreshTokenValidityInMilliseconds);
        return new Token(token, refreshTokenValidityInMilliseconds);
    }

    public String createToken(String payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload); // subject에 대한 내용을 가진 Claim 생성
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public String getPayload(String token){ // MemberId 반환
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e){
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
        }
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}