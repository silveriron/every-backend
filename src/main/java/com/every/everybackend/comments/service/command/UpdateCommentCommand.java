package com.every.everybackend.comments.service.command;

import com.every.everybackend.users.entity.UserEntity;

public record UpdateCommentCommand(Long commentId, Long postId, String content, UserEntity user) {
}
