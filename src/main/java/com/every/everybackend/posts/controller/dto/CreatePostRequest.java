package com.every.everybackend.posts.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
