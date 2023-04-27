package com.example.oauthservice.config;

import com.example.oauthservice.config.handler.RestAuthenticationEntryPoint;
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

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // rest api 서버로 사용하면 csrf 보안이 필요없으므로 csrf 비활성화
                .csrf().disable()

                // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // form login 제거
                .and().formLogin().disable()

                // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .httpBasic().disable()

                // ErrorHandling에 인증되지 않았을 때의 동작을 AuthenticationEntryPoint를 설정하여 제어
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
    }
    
}
