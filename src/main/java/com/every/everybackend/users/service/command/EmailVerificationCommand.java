package com.every.everybackend.users.service.command;

public record EmailVerificationCommand(
        String email,
        String code
) {
}
