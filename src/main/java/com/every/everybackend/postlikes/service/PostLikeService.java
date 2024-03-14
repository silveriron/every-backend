package com.every.everybackend.postlikes.service;

import com.every.everybackend.postlikes.entity.PostLikeEntity;
import com.every.everybackend.postlikes.repository.PostLikeRepository;
import com.every.everybackend.postlikes.service.command.GetLikesCommand;
import com.every.everybackend.postlikes.service.command.LikePostCommand;
import com.every.everybackend.postlikes.service.command.UnlikePostCommand;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.posts.service.PostService;
import com.every.everybackend.posts.service.command.GetPostsCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

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

    @Transactional(readOnly = true)
    public List<PostEntity> getLikes(GetLikesCommand command) {
        List<PostLikeEntity> postLikeEntities = postLikeRepository.findAllByUserId(command.user().getId());


        List<Long> postIds = postLikeEntities.stream().map(PostLikeEntity::getPostId).toList();

        GetPostsCommand getPostsCommand = new GetPostsCommand(postIds);

        return postService.getPosts(getPostsCommand);
    }
}
