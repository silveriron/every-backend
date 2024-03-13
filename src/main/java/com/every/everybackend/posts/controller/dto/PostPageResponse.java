package com.every.everybackend.posts.controller.dto;

import java.util.List;

public record PostPageResponse(
        List<PostResponse> posts,
        int totalPages,
        long totalElements,
        int currentPage,
        boolean hasNext,
        boolean hasPrevious
) {
}
