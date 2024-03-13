package com.every.everybackend.comments.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank
        String content
) {
}
