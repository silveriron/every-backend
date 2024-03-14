package com.every.everybackend.subcribes.entity;

import com.every.everybackend.users.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "subscribes")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class SubscribeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public SubscribeEntity(UserEntity user, UserEntity author) {
        this.user = user;
        this.author = author;
    }
}
