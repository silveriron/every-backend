package com.every.everybackend.common.exception.dto;


import com.every.everybackend.common.exception.errorcode.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private final Integer status;
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.code = "E000";
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
