package com.example.oauthservice.config;

import com.example.oauthservice.config.handler.LoginSuccessHandler;
import com.example.oauthservice.config.handler.RestAccessDeniedHandler;
import com.example.oauthservice.config.handler.RestAuthenticationEntryPoint;
import com.example.oauthservice.service.CustomOauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // Controller에서 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 @PreAuthorize 어노테이션을 사용하는데, 해당 어노테이션에 대한 설정을 활성화시키는 어노테이션 => 특정 주소로 접근을 하면 권한 및 인증을 미리 체크
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomOauth2Service customOauth2Service;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // rest api 서버로 사용하면 csrf 보안이 필요없으므로 csrf 비활성화
                .csrf().disable()

                // jwt token으로 인증할것이므로 세션이 필요없으므로 생성X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // form login 제거
                .and().formLogin().disable()

                // rest api 이므로 기본설정 사용안함(기본설정은 비인증시 로그인폼 화면으로 리다이렉트 됨)
                .httpBasic().disable()

                // 예외처리 기능 작동
                .exceptionHandling()

                // Security 필터에 의해 발생하는 예외는 2가지가 있음
                // Security 필터들 중 마지막에 위치한 FilterSecurityInterceptor가 예외를 발생시킴
                // 2가지 예외 : AuthenticationException(인증 실패), AccessDeniedException(인가 실패)
                // FilterSecurityInterceptor에서 발생한 예외 처리는 ExceptionTranslationFilter(FilterSecurityInterceptor 앞에 위치한 필터)가 수행
                // ExceptionTranslationFilter가 사용자의 요청을 박아 다음 필터에게 전달할 때 try catch {}로 감싸서 FilterSecurityInterceptor를 호출
                // ExceptionTranslationFilter는 예외 발생시 AuthenticationEntryPoint(인증 실패), AccessDeniedHandler(인가 실패)를 실행함
                .authenticationEntryPoint(restAuthenticationEntryPoint).accessDeniedHandler(new RestAccessDeniedHandler())

                // 소셜로그인 설정 사용
                .and().oauth2Login()

                // oauth2 로그인 성공시 호출할 handler 지정
                .successHandler(loginSuccessHandler)

                // oauth2 로그인 성공 후의 설정, Provider로부터 획득한 유저정보를 다룰 service class를 지정
                // userInfoEndpoint().userService()가 먼저 실행되고, 그 이후에 successHandler()가 실행됨
                .userInfoEndpoint().userService(customOauth2Service);

    }
}
