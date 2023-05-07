package com.timecapsule.oauthservice.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
// HTTP 요청으로 부터 JWT를 추출하는 클래스
public class AuthorizationExtractor {

    public static final String AUTHORIZATION = "Authorization";
    public static String BEARER_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            log.info("value = ", value);
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                return authHeaderValue;
            }
        }
        return null;
    }
}