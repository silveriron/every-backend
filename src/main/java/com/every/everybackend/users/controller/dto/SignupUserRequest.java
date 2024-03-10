package com.every.everybackend.users.controller.dto;

public record SignupUserRequest(
   String email,
    String password,
    String name,
   String imageUrl
) {
}
