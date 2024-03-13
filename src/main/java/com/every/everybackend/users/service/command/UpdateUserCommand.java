package com.every.everybackend.users.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record UpdateUserCommand(
        UserEntity userEntity,
        String password,
        String name,
        String imageUrl
) {
}
