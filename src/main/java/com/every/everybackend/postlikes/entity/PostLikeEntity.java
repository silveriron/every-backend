package com.every.everybackend.postlikes.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "post_likes")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class PostLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long postId;
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public PostLikeEntity(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
