package com.every.everybackend.users.repository;

import com.every.everybackend.base.RepositoryTest;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일로 사용자 정보를 찾는다")
    @Test
    void findByEmail() {

        // given
        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .password("1234")
                .name("test")
                .role(UserRole.USER)
                .provider(UserProvider.EMAIL)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(userEntity);

        // when
        Optional<UserEntity> findUser = userRepository.findByEmail("test@test.com");

        // then
        assertTrue(findUser.isPresent());
        assertEquals(findUser.get().getEmail(), "test@test.com");
    }

}