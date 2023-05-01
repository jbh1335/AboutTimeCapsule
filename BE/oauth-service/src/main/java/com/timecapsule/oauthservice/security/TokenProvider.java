package com.timecapsule.oauthservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
// JWT 토큰을 생성, 파싱, 유효성 검사를 위한 클래스
public class TokenProvider {
    /* [토큰의 형태]
    {
	    Authorization : Bearer +토큰
    }
    * */
    public static final String TOKEN_TYPE = "Bearer ";
    private final String secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public TokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-expiration-time}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-expiration-time}") long refreshTokenExpirationTime) {
        this.secretKey = secretKey;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenExpirationTime);
    }

    public String createRefreshToken(Authentication authentication){
        return createToken(authentication, refreshTokenExpirationTime);
    }

    private String createToken(Authentication authentication, long accessTokenExpirationTimeInMilliSeconds) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationTimeInMilliSeconds);

        return Jwts.builder()
                .setSubject(userPrincipal.getUserName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public long getRemainingMilliSecondsFromToken(String token){
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - (new Date()).getTime();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 유효한 토큰인지 확인
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}