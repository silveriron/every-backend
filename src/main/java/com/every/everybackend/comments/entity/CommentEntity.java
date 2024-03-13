package com.every.everybackend.comments.entity;

import com.every.everybackend.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private Long postId;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public CommentEntity(String content, UserEntity user, Long postId) {
        this.content = content;
        this.user = user;
        this.postId = postId;
    }

    public void update(String content) {
        this.content = content;
    }
}
