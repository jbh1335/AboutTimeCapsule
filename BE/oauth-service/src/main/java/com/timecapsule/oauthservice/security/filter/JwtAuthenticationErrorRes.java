package com.timecapsule.oauthservice.security.filter;

import com.timecapsule.oauthservice.api.response.CommonRes;
import com.timecapsule.oauthservice.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtAuthenticationErrorRes extends CommonRes {
    private int status;
    private String code;

    public JwtAuthenticationErrorRes(ErrorCode errorCode) {
        super(false, errorCode.getDetailMessage());
        this.status = errorCode.getHttpStatus().value();
        this.code = errorCode.name();
    }

    public static JwtAuthenticationErrorRes of(ErrorCode errorCode) {
        return new JwtAuthenticationErrorRes(errorCode);
    }
}
