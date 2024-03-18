package com.every.everybackend.comments.controller;

import com.every.everybackend.base.MvcTest;
import com.every.everybackend.comments.controller.dto.CreateCommentRequest;
import com.every.everybackend.comments.controller.dto.UpdateCommentRequest;
import com.every.everybackend.comments.entity.CommentEntity;
import com.every.everybackend.comments.repository.CommentRepository;
import com.every.everybackend.posts.entity.PostEntity;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends MvcTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .name("test")
                .email("test@test.com")
                .password("test")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .provider(UserProvider.EMAIL)
                .build());

        PostEntity postEntity = postRepository.save(PostEntity.builder()
                .author(userEntity)
                .title("test")
                .content("test")
                .build());

        commentRepository.save(CommentEntity.builder()
                .postId(postEntity.getId())
                .user(userEntity)
                .content("test")
                .build());
    }

    @DisplayName("게시물에 댓글을 작성한다")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createComment() throws Exception {
        // given
        List<PostEntity> postEntities = postRepository.findAll();
        Long postId = postEntities.get(0).getId();
        String content = "test";

        // when & then
        mockMvc.perform(post("/api/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateCommentRequest(content))))
                .andExpect(status().isOk())
                .andDo(document("comment-create/success"));

    }

    @DisplayName("게시물에 댓글을 조회한다")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getComments() throws Exception {
        // given
        List<PostEntity> postEntities = postRepository.findAll();
        Long postId = postEntities.get(0).getId();

        // when & then
        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andDo(document(
                        "comment-get/success",
                        pathParameters(
                                parameterWithName("postId").description("게시물 아이디")
                        )));
    }

    @DisplayName("게시물에 댓글을 수정한다")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void updateComment() throws Exception {
        // given
        List<PostEntity> postEntities = postRepository.findAll();
        Long postId = postEntities.get(0).getId();
        List<CommentEntity> commentEntities = commentRepository.findAll();
        Long commentId = commentEntities.get(0).getId();
        String content = "new comment";

        // when & then
        mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateCommentRequest(content))))
                .andExpect(status().isOk())
                .andDo(document("comment-update/success", pathParameters(
                        parameterWithName("postId").description("게시물 아이디"),
                        parameterWithName("commentId").description("댓글 아이디")
                )));
    }

    @DisplayName("게시물에 댓글을 삭제한다")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteComment() throws Exception {
        // given
        List<PostEntity> postEntities = postRepository.findAll();
        Long postId = postEntities.get(0).getId();
        List<CommentEntity> commentEntities = commentRepository.findAll();
        Long commentId = commentEntities.get(0).getId();

        // when & then
        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk())
                .andDo(document("comment-delete/success", pathParameters(
                        parameterWithName("postId").description("게시물 아이디"),
                        parameterWithName("commentId").description("댓글 아이디")
                )));
    }
}