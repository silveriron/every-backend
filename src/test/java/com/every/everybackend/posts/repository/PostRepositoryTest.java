package com.every.everybackend.posts.repository;

import com.every.everybackend.base.RepositoryTest;
import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import com.every.everybackend.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostRepositoryTest extends RepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    UserEntity userEntity = UserEntity.builder()
        .email("test@test.com")
        .password("1234")
        .name("test")
        .provider(UserProvider.EMAIL)
        .status(UserStatus.ACTIVE)
        .role(UserRole.USER)
        .build();

    UserEntity savedUserEntity = userRepository.save(userEntity);

    List<String> list = List.of("1", "2", "3");

    List<PostEntity> postEntities = list.stream().map(it -> {
      return PostEntity.builder()
          .title("test" + it)
          .content("test" + it)
          .author(savedUserEntity)
          .build();
    }).toList();

    postRepository.saveAll(postEntities);
  }
  @DisplayName("게시글을 페이지네이션하여 조회한다")
  @Test
  void findAll() {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    // when
    var posts = postRepository.findAll(pageable);
    // then
    assertEquals(posts.getTotalElements(), 3);
    assertFalse(posts.hasNext());
    assertEquals(posts.getTotalPages(), 1);
  }

  @DisplayName("게시글 id와 작성자로 게시글을 조회한다")
  @Test
  void findByIdAndAuthor() {
    // given
    UserEntity userEntity = userRepository.findByEmail("test@test.com").orElseThrow();

    List<PostEntity> postEntities = postRepository.findAll();

    Long postId = postEntities.get(0).getId();

    // when
    var post = postRepository.findByIdAndAuthor(postId, userEntity);

    // then
    assertTrue(post.isPresent());
    assertEquals(post.get().getTitle(), "test1");
  }
}