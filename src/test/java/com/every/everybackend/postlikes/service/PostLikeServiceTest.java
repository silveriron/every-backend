package com.every.everybackend.postlikes.service;

import com.every.everybackend.base.MockTest;
import com.every.everybackend.postlikes.entity.PostLikeEntity;
import com.every.everybackend.postlikes.repository.PostLikeRepository;
import com.every.everybackend.postlikes.service.command.GetLikesCommand;
import com.every.everybackend.postlikes.service.command.LikePostCommand;
import com.every.everybackend.postlikes.service.command.UnlikePostCommand;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.posts.service.PostService;
import com.every.everybackend.users.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostLikeServiceTest extends MockTest {

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostLikeService postLikeService;

    @DisplayName("게시글 Id와 유저 Id를 받아 좋아요를 추가한다.")
    @Test
    void likePost() {
        // given

        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        LikePostCommand command = new LikePostCommand(1L, userEntity);
        // when
        postLikeService.likePost(command);
        // then
        verify(postLikeRepository).save(any());
    }

    @DisplayName("게시글 Id와 유저 Id를 받아 좋아요를 취소한다.")
    @Test
    void unlikePost() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        UnlikePostCommand command = new UnlikePostCommand(1L, userEntity);

        // when
        postLikeService.unlikePost(command);

        // then
        verify(postLikeRepository).deleteByPostIdAndUserId(command.postId(), command.user().getId());
    }

    @DisplayName("유저 Id를 받아 좋아요한 게시글 목록을 반환한다.")
    @Test
    void getLikes() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        PostEntity postEntity = PostEntity.builder()
                .author(userEntity)
                .title("title")
                .content("content")
                .build();

        PostLikeEntity postLikeEntity = PostLikeEntity.builder()
                .postId(1L)
                .userId(1L)
                .build();

        GetLikesCommand command = new GetLikesCommand(userEntity);

        when(postLikeRepository.findAllByUserId(command.user().getId())).thenReturn(List.of(postLikeEntity));
        when(postService.getPosts(any())).thenReturn(List.of(postEntity));

        // when
        List<PostEntity> likes = postLikeService.getLikes(command);

        // then
        assertEquals(likes.size(), 1);
        assertEquals(likes.get(0).getTitle(), "title");
    }
}