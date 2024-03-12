package com.every.everybackend.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode{

    USER_NOT_FOUND(404, "U001", "유저를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(400, "U002", "이미 존재하는 유저입니다."),
    NOT_VERIFIED_USER(401, "U003", "인증이 되지 않은 유저입니다."),
    ALREADY_REGISTERED_PHONE_NUMBER(400, "U004", "이미 등록된 전화번호입니다."),
    ALREADY_VERIFIED_USER(400, "U005", "이미 인증된 유저입니다."),
    DELETED_USER(400, "U006", "탈퇴한 유저입니다.")
    ;

    private final Integer status;
    private final String code;
    private final String message;
}
