package com.every.everybackend.users.controller.dto;

public record UpdateUserRequest(
        String password,
        String name,
        String imageUrl
) {
}
