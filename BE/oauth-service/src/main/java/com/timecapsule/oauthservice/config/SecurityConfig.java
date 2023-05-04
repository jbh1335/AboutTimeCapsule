package com.timecapsule.oauthservice.config;

import com.timecapsule.oauthservice.security.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Bean // authenticationManager Bean 등록
    @Override
    // Spring Security에서 모든 인증은 AuthenticationManager를 통해 이루어짐
    // 로그인 처리 즉, 인증을 위해서는 UserDetailService를 통해서 필요한 정보들을 가져오는데
    // 서비스 클래스에서는 UserDetailsService 인터페이스를 implements하여, loadUserByUsername() 메서드를 구현하면 됨
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // rest api 이므로 기본설정 사용안함(기본설정은 비인증시 로그인폼 화면으로 리다이렉트 됨)
                .httpBasic().disable()

                // rest api 서버로 사용하면 csrf 보안이 필요없으므로 csrf 보안 토큰 비활성화
                .csrf().disable()

                // jwt token으로 인증할것이므로 세션이 필요없으므로 생성X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // form login 제거
                .and().formLogin().disable()

                // 지정된 필터 앞에 커스텀 필터를 추가
                // UsernamePasswordAuthenticationFilter보다 앞에 token 인증 관련된 로직을 적용시켜서, 인증 완료후 해당 객체에 권한을 부여해야 함
                // 따라서 token 인증 관련된 로직이 먼저 적용되도록 필터를 등록 시킴
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider,redisUtil),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
