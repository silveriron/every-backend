package com.every.everybackend.comments.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        String username,
        String userImageUrl,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
