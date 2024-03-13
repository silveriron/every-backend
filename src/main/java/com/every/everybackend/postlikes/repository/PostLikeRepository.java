package com.every.everybackend.postlikes.repository;

import com.every.everybackend.postlikes.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    void deleteByPostIdAndUserId(Long postId, Long userId);
}
