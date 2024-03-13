package com.every.everybackend.users.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record DeletePostCommand(
        Long id,
        UserEntity user
) {
}
