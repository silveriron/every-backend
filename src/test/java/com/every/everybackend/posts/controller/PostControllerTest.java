package com.every.everybackend.posts.controller;

import com.every.everybackend.base.MvcTest;
import com.every.everybackend.posts.controller.dto.CreatePostRequest;
import com.every.everybackend.posts.controller.dto.UpdatePostRequest;
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

class PostControllerTest extends MvcTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    UserEntity userEntity = UserEntity.builder()
        .email("test@test.com")
        .name("test")
        .password("test")
        .status(UserStatus.ACTIVE)
        .provider(UserProvider.EMAIL)
        .role(UserRole.USER)
        .build();

    UserEntity savedUserEntity = userRepository.save(userEntity);

    List<String> list = List.of("1", "2", "3");

    List<PostEntity> postEntities = list.stream().map(it -> PostEntity.builder()
        .title("title" + it)
        .content("content" + it)
        .author(savedUserEntity)
        .build()).toList();

    postRepository.saveAll(postEntities);
  }

  @DisplayName("제목과 타이틀로 게시글 생성을 요청하면 사용자가 작성자인 게시글이 생성된다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void createPost() throws Exception {
    // given
    String title = "new title";
    String content = "new content";

    CreatePostRequest request = new CreatePostRequest(title, content);

    // when & then
    mockMvc.perform(
        post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andDo(document("post-create/success")
        );
  }

  @DisplayName("게시글 아이디로 게시글을 조회하면 해당 게시글이 조회된다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void getPost() throws Exception {
    // given

    List<PostEntity> postEntities = postRepository.findAll();

    Long id = postEntities.get(0).getId();


    // when
    mockMvc.perform(
        get("/api/posts/{id}", id))
        .andExpect(status().isOk())
        .andDo(document("post-get/success", pathParameters(
            parameterWithName("id").description("게시글 아이디")
          ))
        );
  }

  @DisplayName("존재하지 않는 게시글 아이디로 게시글을 조회하면 404 에러가 발생한다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void getPost_NotFound() throws Exception {
    // given
    Long id = 100L;

    // when
    mockMvc.perform(
        get("/api/posts/{id}", id))
        .andExpect(status().isNotFound())
        .andDo(document("post-get/not-found", pathParameters(
            parameterWithName("id").description("게시글 아이디")
          ))
        );
  }

  @DisplayName("모든 게시글을 조회하면 게시글 목록이 조회된다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void getAllPosts() throws Exception {
    // given
    int page = 0;
    int size = 10;

    // when
    mockMvc.perform(
        get("/api/posts")
        .param("page", String.valueOf(page))
        .param("size", String.valueOf(size)))
        .andExpect(status().isOk())
        .andDo(document("post-get-all/success"));
  }

  @DisplayName("게시글 아이디로 게시글을 수정하면 해당 게시글이 수정된다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void updatePost() throws Exception {
    // given
    List<PostEntity> postEntities = postRepository.findAll();

    Long id = postEntities.get(0).getId();
    String title = "updated title";
    String content = "updated content";

    UpdatePostRequest request = new UpdatePostRequest(title, content);

    // when
    mockMvc.perform(
        put("/api/posts/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(document("post-update/success", pathParameters(
            parameterWithName("id").description("게시글 아이디")
          ))
        );
  }

  @DisplayName("존재하지 않는 게시글 아이디로 게시글을 수정하면 404 에러가 발생한다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void updatePost_NotFound() throws Exception {
    // given
    Long id = 100L;
    String title = "updated title";
    String content = "updated content";

    UpdatePostRequest request = new UpdatePostRequest(title, content);

    // when
    mockMvc.perform(
        put("/api/posts/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andDo(document("post-update/not-found", pathParameters(
            parameterWithName("id").description("게시글 아이디")
          ))
        );
  }

  @DisplayName("게시글 아이디로 게시글을 삭제하면 해당 게시글이 삭제된다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void deletePost() throws Exception {
    // given
    List<PostEntity> postEntities = postRepository.findAll();

    Long id = postEntities.get(0).getId();

    // when
    mockMvc.perform(
        delete("/api/posts/{id}", id))
        .andExpect(status().isOk())
        .andDo(document("post-delete/success", pathParameters(
            parameterWithName("id").description("게시글 아이디")
          ))
        );
  }

  @DisplayName("존재하지 않는 게시글 아이디로 게시글을 삭제하면 404 에러가 발생한다.")
  @Test
  @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void deletePost_NotFound() throws Exception {
    // given
    Long id = 100L;

    // when
    mockMvc.perform(
        delete("/api/posts/{id}", id))
        .andExpect(status().isNotFound())
        .andDo(document("post-delete/not-found", pathParameters(
            parameterWithName("id").description("게시글 아이디")
          ))
        );
  }
}