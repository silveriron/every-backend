package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
