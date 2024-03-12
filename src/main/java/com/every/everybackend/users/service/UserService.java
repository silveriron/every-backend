package com.every.everybackend.users.service;

import com.every.everybackend.common.adapter.JwtAdapter;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.repository.UserRepository;
import com.every.everybackend.users.repository.entity.UserEntity;
import com.every.everybackend.users.repository.entity.enums.UserProvider;
import com.every.everybackend.users.repository.entity.enums.UserRole;
import com.every.everybackend.users.repository.entity.enums.UserStatus;
import com.every.everybackend.users.service.command.CreateUserCommand;
import com.every.everybackend.users.service.command.LoginUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtAdapter jwtAdapter;


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
  }

  public String login(LoginUserCommand command) {

    UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(command.email(), command.password());

    Authentication authenticate = authenticationManager.authenticate(authenticationToken);

    CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

    return jwtAdapter.createToken(userDetails.getUsername());


  }
}
