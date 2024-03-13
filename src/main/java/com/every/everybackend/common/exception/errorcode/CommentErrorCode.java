package com.every.everybackend.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode{

    NOT_FOUND_COMMENT(404, "C001", "댓글을 찾을 수 없습니다.");

    private final Integer status;
    private final String code;
    private final String message;
}
