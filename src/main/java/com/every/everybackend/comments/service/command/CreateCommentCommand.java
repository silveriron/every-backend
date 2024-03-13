package com.every.everybackend.comments.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record CreateCommentCommand(Long postId, String content, UserEntity user) {
}
