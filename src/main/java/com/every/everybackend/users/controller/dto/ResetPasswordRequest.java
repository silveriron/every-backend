package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String verifyCode,
        @NotBlank
        String newPassword
) {
}
