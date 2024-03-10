package com.every.everybackend.users.repository.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserProvider {

  EMAIL("EMAIL"), GOOGLE("GOOGLE");

  private final String value;
}
