package com.every.everybackend.common.exception.errorcode;

public interface ErrorCode {

    Integer getStatus();
    String getCode();
    String getMessage();
}
