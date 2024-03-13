package com.every.everybackend.posts.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record CreatePostCommand(
        String title,
        String content,
        UserEntity user
) {
}
