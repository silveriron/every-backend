package com.every.everybackend.posts.entity;

import com.every.everybackend.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    private Long views;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public PostEntity(String title, String content, UserEntity author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addViews() {
        this.views++;
    }
}
