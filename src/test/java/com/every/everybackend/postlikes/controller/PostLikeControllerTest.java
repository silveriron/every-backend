package com.every.everybackend.postlikes.controller;

import com.every.everybackend.base.MvcTest;
import com.every.everybackend.postlikes.entity.PostLikeEntity;
import com.every.everybackend.postlikes.repository.PostLikeRepository;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostLikeControllerTest extends MvcTest {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .name("test")
                .password("test")
                .role(UserRole.USER)
                .provider(UserProvider.EMAIL)
                .status(UserStatus.ACTIVE)
                .build());

        PostEntity postEntity = postRepository.save(PostEntity.builder()
                .author(userEntity)
                .title("title")
                .content("content")
                .build());

        postLikeRepository.save(PostLikeEntity.builder()
                .postId(postEntity.getId())
                .userId(userEntity.getId())
                .build());
    }

    @DisplayName("사용자는 게시글을 좋아요 표시할 수 있다.")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void likePost() throws Exception {
        // given
        List<PostEntity> postEntities = postRepository.findAll();

        Long postId = postEntities.get(0).getId();

        // when & then
        mockMvc.perform(post("/api/posts/{postId}/likes", postId))
                .andExpect(status().isOk())
                .andDo(document("post-like", pathParameters(
                        parameterWithName("postId").description("게시글 ID")
                )));
    }

    @DisplayName("사용자는 게시글 좋아요를 취소할 수 있다.")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unlikePost() throws Exception {
        // given
        List<PostEntity> postEntities = postRepository.findAll();

        Long postId = postEntities.get(0).getId();

        // when & then
        mockMvc.perform(post("/api/posts/{postId}/likes", postId))
                .andExpect(status().isOk())
                .andDo(document("post-unlike", pathParameters(
                        parameterWithName("postId").description("게시글 ID")
                )));
    }

    @DisplayName("사용자는 좋아요한 게시글 목록을 조회할 수 있다.")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getLikes() throws Exception {
        // when & then
        mockMvc.perform(get("/api/users/likes"))
                .andExpect(status().isOk())
                .andDo(document("get-likes"));
    }

}