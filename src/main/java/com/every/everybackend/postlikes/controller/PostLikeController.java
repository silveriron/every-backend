package com.every.everybackend.postlikes.controller;

import com.every.everybackend.postlikes.service.PostLikeService;
import com.every.everybackend.postlikes.service.command.GetLikesCommand;
import com.every.everybackend.postlikes.service.command.LikePostCommand;
import com.every.everybackend.postlikes.service.command.UnlikePostCommand;
import com.every.everybackend.posts.controller.dto.PostResponse;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("posts/{postId}/likes")
    public void likePost(
            @PathVariable(value = "postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserEntity user = userDetails.getUser();

        LikePostCommand command = new LikePostCommand(postId, user);

        postLikeService.likePost(command);
    }

    @DeleteMapping("posts/{postId}/likes")
    public void unlikePost(
            @PathVariable(value = "postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserEntity user = userDetails.getUser();

        UnlikePostCommand command = new UnlikePostCommand(postId, user);

        postLikeService.unlikePost(command);
    }

    @GetMapping("/users/likes")
    public ResponseEntity<List<PostResponse>> getLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserEntity user = userDetails.getUser();

        GetLikesCommand command = new GetLikesCommand(user);

        List<PostEntity> posts = postLikeService.getLikes(command);

        List<PostResponse> postResponseList = posts.stream().map(post -> PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getName())
                .authorImageUrl(post.getAuthor().getImage())
                .views(post.getViews())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build()).toList();

        return ResponseEntity.ok(postResponseList);

    }
}
