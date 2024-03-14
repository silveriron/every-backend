package com.every.everybackend.subcribes.controller;

import com.every.everybackend.subcribes.service.SubscribeService;
import com.every.everybackend.subcribes.service.command.CreateSubscribeCommand;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/{authorId}")
    public void subscribe(@PathVariable(value = "authorId") Long authorId,
                          @AuthenticationPrincipal CustomUserDetails userDetails
                          ) {

        UserEntity user = userDetails.getUser();

        CreateSubscribeCommand command = new CreateSubscribeCommand(authorId, user);

        subscribeService.subscribe(command);
    }
}
