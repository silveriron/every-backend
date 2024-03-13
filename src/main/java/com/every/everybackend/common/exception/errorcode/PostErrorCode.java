package com.every.everybackend.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCode{

    NOT_FOUND_POST(404, "P001", "게시글을 찾을 수 없습니다.");

    private final Integer status;
    private final String code;
    private final String message;
}
