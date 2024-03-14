package com.every.everybackend.subcribes.service;

import com.every.everybackend.subcribes.entity.SubscribeEntity;
import com.every.everybackend.subcribes.repository.SubscribeRepository;
import com.every.everybackend.subcribes.service.command.CreateSubscribeCommand;
import com.every.everybackend.subcribes.service.command.DeleteSubscribeCommand;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserService userService;

    public void subscribe(CreateSubscribeCommand command) {

        UserEntity author = userService.findById(command.authorId());

        SubscribeEntity subscribeEntity = SubscribeEntity.builder()
                .user(command.user())
                .author(author)
                .build();

        subscribeRepository.save(subscribeEntity);
    }

    public void unsubscribe(DeleteSubscribeCommand command) {

            UserEntity author = userService.findById(command.authorId());

            subscribeRepository.deleteByUserAndAuthor(command.user(), author);
    }
}
