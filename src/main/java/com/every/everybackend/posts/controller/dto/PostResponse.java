package com.every.everybackend.posts.controller.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
        Long id,
        String title,
        String content,
        String authorName,
        String authorImageUrl,
        Long views,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
