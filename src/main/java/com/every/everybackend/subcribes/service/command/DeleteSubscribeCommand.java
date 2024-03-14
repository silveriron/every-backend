package com.every.everybackend.subcribes.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record DeleteSubscribeCommand(Long authorId, UserEntity user) {
}
