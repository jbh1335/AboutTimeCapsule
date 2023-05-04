package com.timecapsule.oauthservice.security;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter // Setter가 있어야 yml 파일의 값이 자동주입됨
@ConfigurationProperties(prefix = "token") // application-{prefix}.yml의 key와 일치하는 멤버변수가 연결되는데 yml 파일의 key값이 user-id 과 같이 중앙 하이픈(-)이 포함된 경우 카멜표기법으로 변환된 key가 멤버변수와 연결
public class JwtProperties {
    private String secretKey;
    private long expirationTime;
    private long logoutTokenExpirationTime;
}
