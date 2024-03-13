package com.every.everybackend.posts.controller.dto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        String authorName,
        String authorImageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
