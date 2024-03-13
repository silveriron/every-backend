package com.every.everybackend.posts.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
