package com.every.everybackend.common.exception.dto;

import com.every.everybackend.common.exception.errorcode.ErrorCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidErrorResponse extends ErrorResponse{

    private final Map<String, String> errors = new HashMap<>();

    public ValidErrorResponse(ErrorCode errorCode, Map<String, String> errors) {
        super(errorCode);
        this.errors.putAll(errors);
    }
}
