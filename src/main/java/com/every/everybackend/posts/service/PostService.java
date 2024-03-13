package com.every.everybackend.posts.service;

import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.PostErrorCode;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.posts.repository.PostRepository;
import com.every.everybackend.posts.service.command.CreatePostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(CreatePostCommand command) {

        PostEntity postEntity = PostEntity.builder()
                .title(command.title())
                .content(command.content())
                .author(command.user())
                .build();

        postRepository.save(postEntity);
    }

    public Page<PostEntity> getAllPosts(PageRequest pageRequest) {

        return postRepository.findAll(pageRequest);

    }

    public PostEntity getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ApiException(PostErrorCode.NOT_FOUND_POST));
    }
}
