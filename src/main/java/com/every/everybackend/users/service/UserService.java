package com.every.everybackend.users.service;

import com.every.everybackend.users.repository.UserRepository;
import com.every.everybackend.users.repository.entity.UserEntity;
import com.every.everybackend.users.repository.entity.enums.UserProvider;
import com.every.everybackend.users.repository.entity.enums.UserRole;
import com.every.everybackend.users.repository.entity.enums.UserStatus;
import com.every.everybackend.users.service.command.CreateUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void createUser(CreateUserCommand command) {

    Optional<UserEntity> optional = userRepository.findByEmail(command.email());

    if (optional.isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    String encoded = passwordEncoder.encode(command.password());


    UserEntity userEntity = UserEntity.builder()
        .email(command.email())
        .password(encoded)
        .name(command.name())
        .image(command.imageUrl())
        .role(UserRole.USER)
        .status(UserStatus.UNVERIFIED)
        .provider(UserProvider.EMAIL)
        .providerId(null)
        .build();

    userRepository.save(userEntity);

    return;
  }
}
