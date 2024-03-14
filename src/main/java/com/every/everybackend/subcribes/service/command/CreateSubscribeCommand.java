package com.every.everybackend.subcribes.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record CreateSubscribeCommand(Long authorId, UserEntity user) {
}
