package com.every.everybackend.users.service.command;

public record LoginUserCommand(
        String email,
        String password
) {
}
