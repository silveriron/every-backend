package com.every.everybackend.subcribes.service;

import com.every.everybackend.base.MockTest;
import com.every.everybackend.subcribes.repository.SubscribeRepository;
import com.every.everybackend.subcribes.service.command.CreateSubscribeCommand;
import com.every.everybackend.subcribes.service.command.DeleteSubscribeCommand;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubscribeServiceTest extends MockTest {

    @Mock
    private SubscribeRepository subscribeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubscribeService subscribeService;

    @DisplayName("사용자 정보와 작성자 Id를 받아 구독을 생성한다.")
    @Test
    void subscribe() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        UserEntity author = UserEntity.builder()
                .email("author@test.com")
                .build();

        Long authorId = 1L;
        CreateSubscribeCommand command = new CreateSubscribeCommand(authorId, userEntity);

        when(userService.findById(authorId)).thenReturn(author);

        // when
        subscribeService.subscribe(command);

        // then
        verify(subscribeRepository).save(any());
    }

    @DisplayName("사용자 정보와 작성자 Id를 받아 구독을 삭제한다.")
    @Test
    void unsubscribe() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .email("test@test.com")
                .build();

        UserEntity author = UserEntity.builder()
                .email("author@test.com")
                .build();

        Long authorId = 1L;
        DeleteSubscribeCommand command = new DeleteSubscribeCommand(authorId, userEntity);

        when(userService.findById(authorId)).thenReturn(author);

        // when
        subscribeService.unsubscribe(command);

        // then
        verify(subscribeRepository).deleteByUserAndAuthor(any(), any());
    }
}