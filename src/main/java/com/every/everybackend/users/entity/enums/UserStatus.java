package com.every.everybackend.users.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

  ACTIVE("ACTIVE"), UNVERIFIED("UNVERIFIED"), DELETED("DELETED");

  private final String value;
}
