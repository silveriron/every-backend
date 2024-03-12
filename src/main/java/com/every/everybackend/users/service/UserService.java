package com.every.everybackend.users.service;

import com.every.everybackend.common.adapter.JwtAdapter;
import com.every.everybackend.common.adapter.MailAdapter;
import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.UserErrorCode;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.repository.UserRepository;
import com.every.everybackend.users.repository.entity.UserEntity;
import com.every.everybackend.users.repository.entity.enums.UserProvider;
import com.every.everybackend.users.repository.entity.enums.UserRole;
import com.every.everybackend.users.repository.entity.enums.UserStatus;
import com.every.everybackend.users.service.command.CreateUserCommand;
import com.every.everybackend.users.service.command.EmailVerificationCommand;
import com.every.everybackend.users.service.command.LoginUserCommand;
import com.every.everybackend.users.utils.CodeGenerator;
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
  private final CodeGenerator codeGenerator;
  private final MailAdapter mailAdapter;


  public void createUser(CreateUserCommand command) {

    Optional<UserEntity> optional = userRepository.findByEmail(command.email());

    if (optional.isPresent()) {
      throw new ApiException(UserErrorCode.USER_ALREADY_EXIST);
    }

    String encoded = passwordEncoder.encode(command.password());

    String code = codeGenerator.generateCode(8);

    UserEntity userEntity = UserEntity.builder()
        .email(command.email())
        .password(encoded)
        .name(command.name())
        .image(command.imageUrl())
        .role(UserRole.USER)
        .status(UserStatus.UNVERIFIED)
        .provider(UserProvider.EMAIL)
        .providerId(null).verifyCode(code)
        .build();

    userRepository.save(userEntity);

    mailAdapter.sendMail(command.email(), "Every 인증 메일입니다.", "http://localhost:8080/api/users/email-verification?email=" + command.email() + "&code=" + code);
  }

  public String login(LoginUserCommand command) {

    UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(command.email(), command.password());

    Authentication authenticate = authenticationManager.authenticate(authenticationToken);

    CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

    return jwtAdapter.createToken(userDetails.getUsername());
  }

  public void verifyEmail(EmailVerificationCommand command) {
    Optional<UserEntity> optional = userRepository.findByEmail(command.email());

    if (optional.isEmpty()) {
      throw new ApiException(UserErrorCode.USER_NOT_FOUND);
    }

    UserEntity userEntity = optional.get();

    if (userEntity.isEmailVerified()) {
      throw new ApiException(UserErrorCode.ALREADY_VERIFIED_USER);
    }

    if (!userEntity.isVerifiedCode(command.code())) {
      throw new ApiException(UserErrorCode.INVALID_CODE);
    }

    userEntity.setStatus(UserStatus.ACTIVE);
    userEntity.clearVerifyCode();

    userRepository.save(userEntity);
  }
}
