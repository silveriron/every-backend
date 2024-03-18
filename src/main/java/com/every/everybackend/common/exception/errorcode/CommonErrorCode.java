package com.every.everybackend.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode{

    INVALID_INPUT_VALUE(400, "C001", "유효하지 않은 입력값입니다.");

    private final Integer status;
    private final String code;
    private final String message;
}
