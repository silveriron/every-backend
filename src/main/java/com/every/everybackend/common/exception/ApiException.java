package com.every.everybackend.common.exception;

import com.every.everybackend.common.exception.dto.ErrorResponse;
import com.every.everybackend.common.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorResponse = new ErrorResponse(errorCode);
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorResponse = new ErrorResponse(errorCode, message);
    }

    public ApiException(String message) {
        super(message);
        this.errorResponse = new ErrorResponse(message);
    }
}
