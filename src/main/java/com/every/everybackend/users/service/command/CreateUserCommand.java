package com.every.everybackend.users.service.command;

public record CreateUserCommand(
    String email,
    String password,
    String name,
    String imageUrl
) {
}
