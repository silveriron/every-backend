package com.every.everybackend.comments.controller;

import com.every.everybackend.comments.controller.dto.CreateCommentRequest;
import com.every.everybackend.comments.service.CommentService;
import com.every.everybackend.comments.service.command.CreateCommentCommand;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

}
