package com.every.everybackend.posts.repository;

import com.every.everybackend.posts.entity.PostEntity;
import com.every.everybackend.users.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @NotNull
    Page<PostEntity> findAll(@NotNull Pageable pageable);
    Optional<PostEntity> findByIdAndAuthor(Long id, UserEntity author);
}
