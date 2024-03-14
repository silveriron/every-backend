package com.every.everybackend.posts.service.command;

import java.util.List;

public record GetPostsCommand(
        List<Long> ids
) {
}
