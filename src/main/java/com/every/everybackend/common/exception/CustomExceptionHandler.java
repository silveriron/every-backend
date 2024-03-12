package com.every.everybackend.common.exception;

import com.every.everybackend.common.exception.dto.ErrorResponse;
import com.every.everybackend.common.exception.errorcode.ServerErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> exception(ApiException e) {
        log.error(e.getErrorResponse().getMessage(), e);
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.valueOf(e.getErrorResponse().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ErrorResponse(ServerErrorCode.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
