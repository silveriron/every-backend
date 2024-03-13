package com.every.everybackend.comments.repository;

import com.every.everybackend.comments.entity.CommentEntity;
import com.every.everybackend.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByPostId(Long id);

    Optional<CommentEntity> findByIdAndPostIdAndUser(Long id, Long postId, UserEntity user);
}
