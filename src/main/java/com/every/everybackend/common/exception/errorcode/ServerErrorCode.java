package com.every.everybackend.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(500, "S000", "서버 내부 오류");

    private final Integer status;
    private final String code;
    private final String message;
}
