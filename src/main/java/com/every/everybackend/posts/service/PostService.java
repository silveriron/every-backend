package com.every.everybackend.posts.service;

import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.PostErrorCode;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.posts.repository.PostRepository;
import com.every.everybackend.posts.service.command.CreatePostCommand;
import com.every.everybackend.posts.service.command.GetPostCommand;
import com.every.everybackend.posts.service.command.UpdatePostCommand;
import com.every.everybackend.users.service.command.DeletePostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    @Transactional(readOnly = true)
    public Page<PostEntity> getAllPosts(PageRequest pageRequest) {

        return postRepository.findAll(pageRequest);

    }

    public PostEntity getPost(GetPostCommand command) {
        PostEntity postEntity = postRepository.findById(command.id()).orElseThrow(() -> new ApiException(PostErrorCode.NOT_FOUND_POST));

        postEntity.addViews();

        return postRepository.save(postEntity);
    }

    public void updatePost(UpdatePostCommand command) {
        PostEntity post = postRepository.findByIdAndAuthor(command.id(), command.user()).orElseThrow(() -> new ApiException(PostErrorCode.NOT_FOUND_POST));

        post.update(command.newTitle(), command.newContent());

        postRepository.save(post);
    }

    public void deletePost(DeletePostCommand command) {

        PostEntity post = postRepository.findByIdAndAuthor(command.id(), command.user()).orElseThrow(() -> new ApiException(PostErrorCode.NOT_FOUND_POST));

        postRepository.delete(post);
    }
}
