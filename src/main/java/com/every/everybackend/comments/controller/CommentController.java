package com.every.everybackend.comments.controller;

import com.every.everybackend.comments.controller.dto.CommentResponse;
import com.every.everybackend.comments.controller.dto.CreateCommentRequest;
import com.every.everybackend.comments.controller.dto.UpdateCommentRequest;
import com.every.everybackend.comments.entity.CommentEntity;
import com.every.everybackend.comments.service.CommentService;
import com.every.everybackend.comments.service.command.CreateCommentCommand;
import com.every.everybackend.comments.service.command.DeleteCommentCommand;
import com.every.everybackend.comments.service.command.GetCommentsCommand;
import com.every.everybackend.comments.service.command.UpdateCommentCommand;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public void createComment(
            @PathVariable(value = "postId") Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {

        UserEntity user = userDetails.getUser();

        CreateCommentCommand command = new CreateCommentCommand(postId, request.content(), user);

        commentService.createComment(command);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable(value = "postId") Long postId
    ) {
        GetCommentsCommand command = new GetCommentsCommand(postId);

        List<CommentEntity> comments = commentService.getComments(command);

        List<CommentResponse> commentResponses = comments.stream().map(it -> CommentResponse.builder()
                        .id(it.getId())
                        .content(it.getContent())
                        .username(it.getUser().getName())
                        .userImageUrl(it.getUser().getImage())
                        .createdAt(it.getCreatedAt())
                        .updatedAt(it.getUpdatedAt())
                        .build())
                .toList();


        return ResponseEntity.ok(commentResponses);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public void updateComment(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserEntity user = userDetails.getUser();

        UpdateCommentCommand command = new UpdateCommentCommand(commentId, postId, request.content(), user);

        commentService.updateComment(command);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public void deleteComment(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserEntity user = userDetails.getUser();

        DeleteCommentCommand command = new DeleteCommentCommand(commentId, postId, user);

        commentService.deleteComment(command);
    }

}
