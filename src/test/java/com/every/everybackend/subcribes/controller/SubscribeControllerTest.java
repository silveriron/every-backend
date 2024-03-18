package com.every.everybackend.subcribes.controller;

import com.every.everybackend.base.MvcTest;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import com.every.everybackend.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SubscribeControllerTest extends MvcTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .name("test")
                .password("test")
                .provider(UserProvider.EMAIL)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build());

        userRepository.save(UserEntity.builder()
                .email("author@test.com")
                .name("author")
                .password("author")
                .provider(UserProvider.EMAIL)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build());
    }

    @DisplayName("작성자 구독하기")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void subscribe() throws Exception {
        // given
        UserEntity user = userRepository.findByEmail("author@test.com").orElseThrow();

        // when & then
        mockMvc.perform(post("/api/subscriptions/{authorId}", user.getId()))
                .andExpect(status().isOk())
                .andDo(document("subscribe/success", pathParameters(
                        parameterWithName("authorId").description("구독할 사용자 ID")
                )));
    }

    @DisplayName("작성자 구독 취소하기")
    @Test
    @WithUserDetails(value = "test@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void unsubscribe() throws Exception {
        // given
        UserEntity user = userRepository.findByEmail("author@test.com").orElseThrow();

        // when & then
        mockMvc.perform(post("/api/subscriptions/{authorId}", user.getId()))
                .andExpect(status().isOk())
                .andDo(document("unsubscribe/success", pathParameters(
                        parameterWithName("authorId").description("구독 취소할 사용자 ID")
                )));
    }
}