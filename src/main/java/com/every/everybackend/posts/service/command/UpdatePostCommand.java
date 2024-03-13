package com.every.everybackend.posts.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record UpdatePostCommand(
        Long id,
        String newTitle,
        String newContent,
        UserEntity user

) {
}
