package com.every.everybackend.users.service.command;

public record SendPasswordRecoveryCodeCommand(
        String email
) {
}
