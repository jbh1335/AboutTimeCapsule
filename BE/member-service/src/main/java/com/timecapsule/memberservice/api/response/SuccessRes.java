package com.timecapsule.memberservice.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessRes<T> extends CommonRes {
    T data;
    public SuccessRes(boolean success, String message, T data) {
        super(success, message);
        this.data = data;
    }
}
