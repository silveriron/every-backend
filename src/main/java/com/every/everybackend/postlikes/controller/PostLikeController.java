package com.every.everybackend.postlikes.controller;

import com.every.everybackend.postlikes.service.PostLikeService;
import com.every.everybackend.postlikes.service.command.LikePostCommand;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/likes")
    public void likePost(
            @PathVariable(value = "postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserEntity user = userDetails.getUser();

        LikePostCommand command = new LikePostCommand(postId, user);

        postLikeService.likePost(command);
    }
}
