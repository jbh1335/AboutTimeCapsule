package com.timecapsule.oauthservice.config.filter;

import com.timecapsule.oauthservice.exception.CustomException;
import com.timecapsule.oauthservice.security.jwt.AuthorizationExtractor;
import com.timecapsule.oauthservice.security.jwt.JwtTokenProvider;
import com.timecapsule.oauthservice.service.AuthService;
import com.timecapsule.oauthservice.util.UserAuthentication;
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
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Set<String> keySet = request.getParameterMap().keySet();
            log.info(keySet.toString());
            String accessToken = AuthorizationExtractor.extract(request);
            log.info("***** accessToken = {}", accessToken);
            if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                int loginMemberId = authService.findMemberByToken(accessToken); // token 검증
                UserAuthentication authentication = new UserAuthentication(loginMemberId); // 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 인증 객체 저장
                log.info("Authentication = {}", authentication.getMemberId());
            }
        } catch (CustomException e) {
            log.info("JwtAuthentication CustomException");
            // Filter에서 request에 Attribute를 추가시켜 Controller 객체가 우선적으로 그 요소를 파악하여 Exception을 핸들링 하도록 함
            request.setAttribute("CustomException", e);
        }
        chain.doFilter(request,response);
    }

}