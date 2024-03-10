package com.every.everybackend.users.controller;

import com.every.everybackend.users.controller.dto.SignupUserRequest;
import com.every.everybackend.users.service.UserService;
import com.every.everybackend.users.service.command.CreateUserCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
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
}
