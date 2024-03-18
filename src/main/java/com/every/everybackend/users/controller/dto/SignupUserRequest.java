package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupUserRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        String imageUrl
) {
}
