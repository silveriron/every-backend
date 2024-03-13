package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank
        String email,
        @NotBlank
        String verifyCode,
        @NotBlank
        String newPassword
) {
}
