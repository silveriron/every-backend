package com.every.everybackend.comments.repository;

import com.every.everybackend.base.RepositoryTest;
import com.every.everybackend.comments.entity.CommentEntity;
import com.every.everybackend.posts.repository.PostRepository;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import com.every.everybackend.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .password("test")
                .name("test")
                .status(UserStatus.ACTIVE)
                .provider(UserProvider.EMAIL)
                .role(UserRole.USER)
                .build());
//
//        PostEntity postEntity = postRepository.save(PostEntity.builder()
//                .title("test")
//                .content("test")
//                .author(userEntity)
//                .build());

        commentRepository.save(CommentEntity.builder()
                .content("test1")
                .postId(1L)
                .user(userEntity)
                .build());

        commentRepository.save(CommentEntity.builder()
                .content("test2")
                .postId(1L)
                .user(userEntity)
                .build());
    }

    @DisplayName("게시글 ID로 게시글에 달린 댓글들을 조회한다.")
    @Test
    void findAllByPostId() {
        // given
        Long postId = 1L;
        // when & then
        assertEquals(2, commentRepository.findAllByPostId(postId).size());
    }

    @DisplayName("댓글 ID, 게시글 ID, 유저로 댓글을 조회한다.")
    @Test
    void findByIdAndPostIdAndUser() {
        // given
        Long commentId = 1L;
        Long postId = 1L;
        UserEntity userEntity = userRepository.findByEmail("test@test.com").orElseThrow();

        // when
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findByIdAndPostIdAndUser(commentId, postId, userEntity);

        // then
        assertTrue(optionalCommentEntity.isPresent());
        assertEquals("test1", optionalCommentEntity.get().getContent());
    }

    @DisplayName("댓글 ID, 게시글 ID, 유저로 댓글을 삭제한다.")
    @Test
    void deleteByIdAndPostIdAndUser() {
        // given
        Long commentId = 1L;
        Long postId = 1L;
        UserEntity userEntity = userRepository.findByEmail("test@test.com").orElseThrow();

        // when
        commentRepository.deleteByIdAndPostIdAndUser(commentId, postId, userEntity);

        // then
        assertFalse(commentRepository.findByIdAndPostIdAndUser(commentId, postId, userEntity).isPresent());
    }
}