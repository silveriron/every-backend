package com.every.everybackend.users.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserProvider {

  EMAIL("EMAIL"), GOOGLE("GOOGLE");

  private final String value;
}
