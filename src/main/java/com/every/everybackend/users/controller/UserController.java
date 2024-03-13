package com.every.everybackend.users.controller;

import com.every.everybackend.users.controller.dto.*;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.repository.entity.UserEntity;
import com.every.everybackend.users.service.UserService;
import com.every.everybackend.users.service.command.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<Object> signup(@Valid @RequestBody SignupUserRequest request) {

    CreateUserCommand command = new CreateUserCommand(
            request.email(),
            request.password(),
            request.name(),
            request.imageUrl()
    );

    userService.createUser(command);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/email-verification")
  public void verifyEmail(
          @RequestParam("email") String email,
            @RequestParam("code") String code
  ) {

    EmailVerificationCommand command = new EmailVerificationCommand(email, code);

    userService.verifyEmail(command);
  }

  @PostMapping("/password-recovery-code")
  public void sendPasswordRecoveryCode(@Valid @RequestBody SendPasswordRecoveryCodeRequest request) {

    SendPasswordRecoveryCodeCommand command = new SendPasswordRecoveryCodeCommand(request.email());

    userService.sendPasswordRecoveryCode(command);
  }

  @PostMapping("/reset-password")
  public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {}

  @PostMapping("/login")
  public String login(
          @Valid
          @RequestBody LoginUserRequest request
  ) {

    LoginUserCommand command = new LoginUserCommand(request.email(), request.password());

    return userService.login(command);
  }

  @PutMapping
  public void updateUser(@Valid @RequestBody UpdateUserRequest request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

    UserEntity user = userDetails.getUser();

    UpdateUserCommand command = new UpdateUserCommand(user, request.password(), request.name(), request.imageUrl());

    userService.updateUser(command);
  }

  @DeleteMapping
  public void deleteUser() {}
}
