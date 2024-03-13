package com.every.everybackend.users.service.command;

public record ResetPasswordCommand(
        String email,
        String verifyCode,
        String newPassword
) {
}
