package com.every.everybackend.postlikes.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record GetLikesCommand(UserEntity user) {
}
