package com.timecapsule.oauthservice.security.filter;

import com.timecapsule.oauthservice.exception.TokenAuthenticationFilterException;
import com.timecapsule.oauthservice.security.SendErrorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationErrorFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (RedisConnectionFailureException e){
            SendErrorUtil.sendServerErrorResponse(response,objectMapper);
        }
        catch (TokenAuthenticationFilterException e) {
            SendErrorUtil.sendUnauthorizedErrorResponse(response,objectMapper);
        }
    }
}