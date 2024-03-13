package com.every.everybackend.comments.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record DeleteCommentCommand(Long commentId, Long postId, UserEntity user) {
}
