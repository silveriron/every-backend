package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupUserRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        String imageUrl
) {
}
