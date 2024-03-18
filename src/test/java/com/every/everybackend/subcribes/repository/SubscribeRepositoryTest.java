package com.every.everybackend.subcribes.repository;

import com.every.everybackend.base.RepositoryTest;
import com.every.everybackend.subcribes.entity.SubscribeEntity;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import com.every.everybackend.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscribeRepositoryTest extends RepositoryTest {

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .name("test")
                .password("test")
                .provider(UserProvider.EMAIL)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build());

        UserEntity authorEntity = userRepository.save(UserEntity.builder()
                .email("author@test.com")
                .name("author")
                .password("author")
                .provider(UserProvider.EMAIL)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build());

        subscribeRepository.save(SubscribeEntity.builder()
                .author(authorEntity)
                .user(userEntity)
                .build());
    }

    @DisplayName("구독 취소")
    @Test
    public void deleteByUserAndAuthor() {
        UserEntity userEntity = userRepository.findByEmail("test@test.com").orElseThrow();

        UserEntity authorEntity = userRepository.findByEmail("author@test.com").orElseThrow();

        subscribeRepository.deleteByUserAndAuthor(userEntity, authorEntity);

        assertEquals(0, subscribeRepository.findAll().size());
    }
}