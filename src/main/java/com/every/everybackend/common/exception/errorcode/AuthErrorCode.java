package com.every.everybackend.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    EXPIRED_TOKEN(401, "T001", "만료된 토큰입니다."),
    INVALID_TOKEN(401, "T002", "유효하지 않은 토큰입니다.");

    private final Integer status;
    private final String code;
    private final String message;
}
