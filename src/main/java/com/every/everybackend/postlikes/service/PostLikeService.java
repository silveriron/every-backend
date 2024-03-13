package com.every.everybackend.postlikes.service;

import com.every.everybackend.postlikes.entity.PostLikeEntity;
import com.every.everybackend.postlikes.repository.PostLikeRepository;
import com.every.everybackend.postlikes.service.command.LikePostCommand;
import com.every.everybackend.postlikes.service.command.UnlikePostCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public void likePost(LikePostCommand command) {

        PostLikeEntity postLikeEntity = PostLikeEntity.builder()
                .postId(command.postId())
                .userId(command.user().getId())
                .build();

        postLikeRepository.save(postLikeEntity);
    }

    public void unlikePost(UnlikePostCommand command) {
        postLikeRepository.deleteByPostIdAndUserId(command.postId(), command.user().getId());
    }
}
