package com.example.oauthservice.security.filter;

import com.example.oauthservice.db.entity.Member;
import com.example.oauthservice.db.repository.MemberRepository;
import com.example.oauthservice.exception.TokenAuthenticationFilterException;
import com.example.oauthservice.exception.UserNotFoundException;
import com.example.oauthservice.security.UserPrincipal;
import com.example.oauthservice.security.TokenProvider;
import com.example.oauthservice.db.repository.jwt.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
// 토큰을 파싱하여, 유저정보를 갖고오고 토큰이 유효할 경우 그 유저에게 권한을 부여하는 클래스
// 토큰을 갖고와서 토큰이 맞으면 해당 유저 정보를 등록 시키고, 인증된 유저정보를 바탕으로 시큐리티에서 사용하는 토큰을 생성하고 등록
// UsernamePasswordAuthenticationFilter 보다 먼저 토큰을 바탕으로 유저를 등록시키는 클래스
// OncePerRequestFilter는 요청당 한번만 일어나도록 하는 추상클래스
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final MemberRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (isValidToken(jwt)) {
                String email = tokenProvider.getUserEmailFromToken(jwt);
                Member user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
                UserPrincipal userPrincipal = UserPrincipal.from(user);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RedisConnectionFailureException e) {
            throw new RedisConnectionFailureException("redis 커넥션에 실패했습니다.");
        } catch(Exception e){
            throw new TokenAuthenticationFilterException();
        }

        filterChain.doFilter(request, response);

    }

    private boolean isValidToken(String jwt) {
        return StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)
                && !tokenRepository.existsLogoutAccessTokenById(jwt) && !tokenRepository.existsLogoutRefreshTokenById(jwt);

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TokenProvider.TOKEN_TYPE)) {
            return bearerToken.substring(7); // 앞의 띄어쓰기 포함 7자를 잘라서 순수 토큰만 추출
        }
        return null;
    }
}