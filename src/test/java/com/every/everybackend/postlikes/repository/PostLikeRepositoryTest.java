package com.every.everybackend.postlikes.repository;

import com.every.everybackend.base.RepositoryTest;
import com.every.everybackend.postlikes.entity.PostLikeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostLikeRepositoryTest extends RepositoryTest {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @BeforeEach
    void setUp() {
        postLikeRepository.save(PostLikeEntity.builder()
                .postId(1L)
                .userId(1L)
                .build());
    }

    @DisplayName("게시물 id와 유저 id로 좋아요 정보를 삭제한다")
    @Test
    void deleteByPostIdAndUserId() {
        // given

        // when
        postLikeRepository.deleteByPostIdAndUserId(1L, 1L);

        // then
        assertEquals(0, postLikeRepository.findAll().size());
    }

    @DisplayName("유저 id로 좋아요 정보를 조회한다")
    @Test
    void findAllByUserId() {
        // given

        // when
        var postLikeEntities = postLikeRepository.findAllByUserId(1L);

        // then
        assertEquals(1, postLikeEntities.size());
        assertEquals(1L, postLikeEntities.get(0).getPostId());
    }
}