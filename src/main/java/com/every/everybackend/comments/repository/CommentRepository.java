package com.every.everybackend.comments.repository;

import com.every.everybackend.comments.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
