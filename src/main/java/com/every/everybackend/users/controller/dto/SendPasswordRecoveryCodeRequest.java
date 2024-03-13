package com.every.everybackend.users.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SendPasswordRecoveryCodeRequest(
        @NotBlank
        String email
) {
}
