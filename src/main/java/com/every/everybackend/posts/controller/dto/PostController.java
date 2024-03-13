package com.every.everybackend.posts.controller.dto;

import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.posts.service.PostService;
import com.every.everybackend.posts.service.command.CreatePostCommand;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Object> createPost(
            @Valid
            @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {

        UserEntity user = userDetails.getUser();

        CreatePostCommand command = new CreatePostCommand(request.title(), request.content(), user);

        postService.createPost(command);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable(value = "id") Long id) {
        PostEntity post = postService.getPost(id);
        return ResponseEntity.ok(new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getName(), post.getAuthor().getImage(), post.getCreatedAt(), post.getUpdatedAt()));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {

        Page<PostEntity> allPosts = postService.getAllPosts(PageRequest.of(page, size));

        List<PostResponse> list = allPosts.stream().map(it -> new PostResponse(it.getId(), it.getTitle(), it.getContent(), it.getAuthor().getName(), it.getAuthor().getImage(), it.getCreatedAt(), it.getUpdatedAt())).toList();

        PostPageResponse postPageResponse = new PostPageResponse(list, allPosts.getTotalPages(), allPosts.getTotalElements(), allPosts.getNumber(), allPosts.hasNext(), allPosts.hasPrevious());

        return ResponseEntity.ok(postPageResponse.posts());
    }


}