package com.every.everybackend.users.repository.entity;

import com.every.everybackend.users.repository.entity.enums.UserProvider;
import com.every.everybackend.users.repository.entity.enums.UserRole;
import com.every.everybackend.users.repository.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "users")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 20)
    private String name;
    private String image;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Enumerated(EnumType.STRING)
    private UserProvider provider;
    private String providerId;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public UserEntity(String email, String password, String name, String image, UserRole role, UserStatus status, UserProvider provider, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
        this.role = role;
        this.status = status;
        this.provider = provider;
        this.providerId = providerId;
    }
}
