package com.timecapsule.oauthservice.config;

import com.timecapsule.oauthservice.config.filter.JwtAuthenticationEntryPoint;
import com.timecapsule.oauthservice.config.filter.JwtAuthenticationFilter;
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
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // rest api 이므로 기본설정 사용안함(기본설정은 비인증시 로그인폼 화면으로 리다이렉트 됨)
                .cors()
                .and()
                .httpBasic().disable()
                // rest api 서버로 사용하면 csrf 보안이 필요없으므로 csrf 보안 토큰 비활성화
                .csrf().disable();

        http
                // URL 별 권한 관리를 설정하는 옵션 사용 => antMatchers
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/login/oauth/**",
                        "/oauth/**",
                        "/token",
                        "/favicon.ico"
                ).permitAll()

                // 설정된 값들 이외의 나머지 URL에 대한 설정 사용
                .anyRequest()
                // 나머지 URL들은 모두 인증된 사용자들에게만 허용
                .authenticated()
                // 예외처리 기능 작동
                .and()
                .exceptionHandling()
                // Security 필터에 의해 발생하는 예외는 2가지가 있음
                // Security 필터들 중 마지막에 위치한 FilterSecurityInterceptor가 예외를 발생시킴
                // 2가지 예외 : AuthenticationException(인증 실패), AccessDeniedException(인가 실패)
                // FilterSecurityInterceptor에서 발생한 예외 처리는 ExceptionTranslationFilter(FilterSecurityInterceptor 앞에 위치한 필터)가 수행
                // ExceptionTranslationFilter가 사용자의 요청을 박아 다음 필터에게 전달할 때 try catch {}로 감싸서 FilterSecurityInterceptor를 호출
                // ExceptionTranslationFilter는 예외 발생시 AuthenticationEntryPoint(인증 실패), AccessDeniedHandler(인가 실패)를 실행함
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // jwt token으로 인증할것이므로 세션이 필요없으므로 생성X
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // form login 제거
                .and()
                .formLogin().disable();

        http
                // 지정된 필터 앞에 커스텀 필터를 추가
                // UsernamePasswordAuthenticationFilter보다 앞에 token 인증 관련된 로직을 적용시켜서, 인증 완료후 해당 객체에 권한을 부여해야 함
                // 따라서 token 인증 관련된 로직이 먼저 적용되도록 필터를 등록 시킴
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    // authenticationManager Bean 등록
    // Spring Security에서 모든 인증은 AuthenticationManager를 통해 이루어짐
    // 로그인 처리 즉, 인증을 위해서는 UserDetailService를 통해서 필요한 정보들을 가져오는데
    // 서비스 클래스에서는 UserDetailsService 인터페이스를 implements하여, loadUserByUsername() 메서드를 구현하면 됨
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    // 스프링 시큐리티 룰을 무시하게 하는 Url 규칙(여기 등록하면 규칙 적용하지 않음)
//    public void configure(WebSecurity web) { // 인증 예외 URL 설정
//        web.ignoring()
//                // antMatchers : 권한관리 대상 설정
//                .antMatchers(HttpMethod.GET, "/login/oauth/**")
//                .antMatchers("/oauth/**")
//                .antMatchers(HttpMethod.POST, "/token");
//    }
}