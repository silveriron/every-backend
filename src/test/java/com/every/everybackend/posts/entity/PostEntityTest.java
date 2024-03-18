package com.every.everybackend.posts.entity;

import com.every.everybackend.users.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostEntityTest {

    @DisplayName("게시글 조회수 증가 테스트")
    @Test
    void addViews() {
        // given
        PostEntity post = PostEntity.builder()
                .title("테스트 게시글")
                .content("테스트 게시글 내용")
                .author(UserEntity.builder()
                        .email("test@test.com")
                        .name("tester")
                        .build())
                .build();

        // when
        post.addViews();

        // then
        assertEquals(post.getViews(), 1);
    }

    @DisplayName("게시글 수정 테스트")
    @Test
    void update() {
        // given
        PostEntity post = PostEntity.builder()
                .title("테스트 게시글")
                .content("테스트 게시글 내용")
                .author(UserEntity.builder()
                        .email("test@test.com")
                        .name("tester")
                        .build())
                .build();

        // when
        post.update("수정된 게시글", "수정된 게시글 내용");

        // then
        assertEquals(post.getTitle(), "수정된 게시글");
        assertEquals(post.getContent(), "수정된 게시글 내용");
    }

}