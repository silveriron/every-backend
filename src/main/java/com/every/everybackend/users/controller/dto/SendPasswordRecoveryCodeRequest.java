package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendPasswordRecoveryCodeRequest(
        @NotBlank
        @Email
        String email
) {
}
