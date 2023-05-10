package com.timecapsule.oauthservice.security.filter;

import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.security.jwt.AuthorizationExtractor;
import com.timecapsule.oauthservice.security.jwt.JwtTokenProvider;
import com.timecapsule.oauthservice.service.TokenService;
import com.timecapsule.oauthservice.security.UserAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    // HTTP 요청(reqeust)에서 JWT을 꺼내 검증한 후 검증이 되면 SecurityContext에 저장
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("{} {} 요청의 인증 시작", request.getMethod(), request.getRequestURI());
        try {
            String accessToken = AuthorizationExtractor.extract(request); // 클라이언트에서 서버에게 Request와 함께 담아 보낸 JWT 추출
            log.info("accessToken = {}", accessToken);
            // StringUtils.hasText(accessToken) : Access Token이 존재하는지 확인
            // jwtTokenProvider.validateToken(accessToken) : Access Token 검증
            if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                int loginMemberId = tokenService.findMemberByToken(accessToken);
                UserAuthentication authentication = new UserAuthentication(loginMemberId); // 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication); // Access Token이 유효하다면 Token에서 유저 정보를 담은 Authentication 객체를 생성해 SecurityContextHolder의 SecurityContext에 저장하여 전역적으로 참조
            }
        } catch (CustomException e) {
            log.info("JwtAuthentication CustomException");
            // Filter에서 request에 Attribute를 추가시켜 Controller 객체가 우선적으로 그 요소를 파악하여 Exception을 핸들링 하도록 함
            request.setAttribute("CustomException", e);
        }
        chain.doFilter(request,response);
    }
}