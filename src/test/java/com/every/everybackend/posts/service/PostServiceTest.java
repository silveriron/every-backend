package com.every.everybackend.posts.service;

import com.every.everybackend.base.MockTest;
import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.PostErrorCode;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.posts.repository.PostRepository;
import com.every.everybackend.posts.service.command.*;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.service.command.DeletePostCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostServiceTest extends MockTest {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostService postService;
  private String title = "test title";
  private String content = "test content";
  private UserEntity userEntity = UserEntity.builder()
      .email("test@test.com")
      .build();

  @DisplayName("게시글 제목과 내용, 작성자를 제공하면 게시글을 생성한다.")
  @Test
  void createPost() {
    // given
    PostEntity postEntity = PostEntity.builder()
        .title(title)
        .content(content)
        .author(userEntity)
        .build();

    CreatePostCommand command = new CreatePostCommand(title, content, userEntity);
    // when
    postService.createPost(command);

    // then
    verify(postRepository).save(postEntity);
  }

  @DisplayName("전체 게시글을 조회하면 게시글 목록을 페이지네이션으로 반환한다.")
  @Test
  void getAllPosts() {
    // given
    List<String> list = List.of("1", "2", "3");

    List<PostEntity> postEntityList = list.stream().map(it -> PostEntity.builder()
        .title(title + it)
        .content(content + it)
        .author(userEntity)
        .build()).toList();


    GetAllPostsCommand command = new GetAllPostsCommand(0, 10);
    when(postRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(postEntityList));

    // when
    Page<PostEntity> allPosts = postService.getAllPosts(command);

    // then
    assertEquals(3, allPosts.getTotalElements());
    assertEquals(1, allPosts.getTotalPages());
  }

  @DisplayName("게시글 id로 조회하면 조회수를 증가시키고 게시글을 반환한다.")
  @Test
  void getPost() {
    // given
    Long id = 1L;
    PostEntity postEntity = PostEntity.builder()
        .title(title)
        .content(content)
        .author(userEntity)
        .build();

    GetPostCommand command = new GetPostCommand(id);

    when(postRepository.findById(id)).thenReturn(java.util.Optional.of(postEntity));
    when(postRepository.save(postEntity)).thenReturn(postEntity);

    // when
    PostEntity post = postService.getPost(command);

    // then
    assertEquals(1, post.getViews());
  }

  @DisplayName("게시글 id 목록으로 조회하면 게시글 목록을 반환한다.")
  @Test
  void getPosts() {
    // given
    List<String> list = List.of("1", "2", "3");

    List<PostEntity> postEntityList = list.stream().map(it -> PostEntity.builder()
        .title(title + it)
        .content(content + it)
        .author(userEntity)
        .build()).toList();

    GetPostsCommand command = new GetPostsCommand(List.of(1L, 2L, 3L));
    when(postRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(postEntityList);

    // when
    List<PostEntity> posts = postService.getPosts(command);

    // then
    assertEquals(3, posts.size());
  }

  @DisplayName("게시글 수정 정보를 제공하면 게시글을 수정한다.")
  @Test
  void updatePost() {
    // given
    Long id = 1L;
    String newTitle = "new title";
    String newContent = "new content";
    PostEntity postEntity = PostEntity.builder()
        .title(title)
        .content(content)
        .author(userEntity)
        .build();

    UpdatePostCommand command = new UpdatePostCommand(id, newTitle, newContent, userEntity);

    when(postRepository.findByIdAndAuthor(id, userEntity)).thenReturn(java.util.Optional.of(postEntity));

    // when
    postService.updatePost(command);

    // then
    verify(postRepository).save(postEntity);
    assertEquals(newTitle, postEntity.getTitle());
    assertEquals(newContent, postEntity.getContent());
  }

  @DisplayName("존재하지 않는 게시글 id로 게시글을 수정하려고 하면 예외를 발생시킨다.")
  @Test
  void updatePostWithNotExistsPost() {
    // given
    Long id = 1L;
    String newTitle = "new title";
    String newContent = "new content";
    UpdatePostCommand command = new UpdatePostCommand(id, newTitle, newContent, userEntity);

    when(postRepository.findByIdAndAuthor(id, userEntity)).thenReturn(java.util.Optional.empty());

    // when
    ApiException apiException = assertThrows(ApiException.class, () -> postService.updatePost(command));

    // then
    assertEquals(apiException.getErrorResponse().getCode(), PostErrorCode.NOT_FOUND_POST.getCode());
  }

  @DisplayName("작성자가 아닌 사용자가 게시글을 수정하려고 하면 예외를 발생시킨다.")
  @Test
  void updatePostWithNotAuthor() {
    // given
    Long id = 1L;
    String newTitle = "new title";
    String newContent = "new content";
    PostEntity postEntity = PostEntity.builder()
        .title(title)
        .content(content)
        .author(UserEntity.builder().email("test2@test.com").build())
        .build();

    UpdatePostCommand command = new UpdatePostCommand(id, newTitle, newContent, userEntity);

    when(postRepository.findByIdAndAuthor(id, userEntity)).thenReturn(java.util.Optional.empty());

    // when
    ApiException apiException = assertThrows(ApiException.class, () -> postService.updatePost(command));

    // then
    assertEquals(apiException.getErrorResponse().getCode(), PostErrorCode.NOT_FOUND_POST.getCode());
  }

  @DisplayName("게시글 id와 작성자를 제공하면 게시글을 삭제한다.")
  @Test
  void deletePost() {
    // given
    Long id = 1L;
    DeletePostCommand command = new DeletePostCommand(id, userEntity);

    when(postRepository.findByIdAndAuthor(id, userEntity)).thenReturn(java.util.Optional.of(PostEntity.builder().build()));

    // when
    postService.deletePost(command);

    // then
    verify(postRepository).delete(PostEntity.builder().build());
  }

  @DisplayName("존재하지 않는 게시글 id로 게시글을 삭제하려고 하면 예외를 발생시킨다.")
  @Test
  void deletePostWithNotExistsPost() {
    // given
    Long id = 1L;
    DeletePostCommand command = new DeletePostCommand(id, userEntity);

    when(postRepository.findByIdAndAuthor(id, userEntity)).thenReturn(java.util.Optional.empty());

    // when
    ApiException apiException = assertThrows(ApiException.class, () -> postService.deletePost(command));

    // then
    assertEquals(apiException.getErrorResponse().getCode(), PostErrorCode.NOT_FOUND_POST.getCode());
  }

  @DisplayName("작성자가 아닌 사용자가 게시글을 삭제하려고 하면 예외를 발생시킨다.")
  @Test
  void deletePostWithNotAuthor() {
    // given
    Long id = 1L;
    DeletePostCommand command = new DeletePostCommand(id, userEntity);

    when(postRepository.findByIdAndAuthor(id, userEntity)).thenReturn(java.util.Optional.empty());

    // when
    ApiException apiException = assertThrows(ApiException.class, () -> postService.deletePost(command));

    // then
    assertEquals(apiException.getErrorResponse().getCode(), PostErrorCode.NOT_FOUND_POST.getCode());
  }
}