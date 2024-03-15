package com.every.everybackend.users.controller;

import com.every.everybackend.base.MvcTest;
import com.every.everybackend.users.controller.dto.*;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import com.every.everybackend.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends MvcTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        UserEntity unverifiedUser = UserEntity.builder()
                .email("unverifiedUser@test.com")
                .name("test")
                .password(passwordEncoder.encode("test"))
                .role(UserRole.USER)
                .provider(UserProvider.EMAIL)
                .providerId("1234")
                .status(UserStatus.UNVERIFIED)
                .verifyCode("12345678")
                .build();

        UserEntity verifiedUser = UserEntity.builder()
                .email("verifiedUser@test.com")
                .name("test2")
                .password(passwordEncoder.encode("test"))
                .role(UserRole.USER)
                .provider(UserProvider.EMAIL)
                .providerId("1234")
                .status(UserStatus.ACTIVE)
                .verifyCode("12345678")
                .build();

        userRepository.saveAll(List.of(unverifiedUser, verifiedUser));
    }

    @DisplayName("가입되지 않은 이메일로 회원가입을 요청하면 회원가입에 성공한다")
    @Test
    void should_RegisterUser_When_RequestRegisterWithUnregisteredEmail() throws Exception {
        // given
        String email = "test@test.com";
        String password = "test";
        String name = "test";

        String body = objectMapper.writeValueAsString(new SignupUserRequest(email, password, name, ""));

        // when
        mockMvc.perform(
                        post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isCreated())
                .andDo(document("users-signup/success"));
    }

    @DisplayName("가입된 이메일로 회원가입을 요청하면 400 에러를 반환한다")
    @Test
    void should_FailToRegisterUser_When_RequestRegisterWithRegisteredEmail() throws Exception {
        // given
        String email = "unverifiedUser@test.com";
        String password = "test";
        String name = "test";

        String body = objectMapper.writeValueAsString(new SignupUserRequest(email, password, name, ""));

        // when
        mockMvc.perform(
                        post("/api/users/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("users-signup/duplicate-email"));
    }

    @DisplayName("가입된 이메일로 로그인을 요청하면 로그인에 성공한다")
    @Test
    void should_LoginUser_When_RequestLoginWithRegisteredEmail() throws Exception {
        // given
        String email = "verifiedUser@test.com";
        String password = "test";

        String body = objectMapper.writeValueAsString(new LoginUserRequest(email, password));

        // when
        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andDo(document("users-login/success"));
    }

    @DisplayName("가입되지 않은 이메일로 로그인을 요청하면 400 에러를 반환한다")
    @Test
    void should_FailToLoginUser_When_RequestLoginWithUnregisteredEmail() throws Exception {
        // given
        String email = "test@test.com";
        String password = "test";

        String body = objectMapper.writeValueAsString(new LoginUserRequest(email, password));

        // when
        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("users-login/not-found-user"));
    }

    @DisplayName("가입된 이메일로 로그인을 요청하나 비밀번호가 틀리면 400 에러를 반환한다")
    @Test
    void should_FailToLoginUser_When_RequestLoginWithRegisteredEmailButWrongPassword() throws Exception {
        // given
        String email = "verifiedUser@test.com";
        String password = "wrongPassword";

        LoginUserRequest request = new LoginUserRequest(email, password);

        String body = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("users-login/wrong-password"));
    }

    @DisplayName("올바른 정보로 이메일 인증을 요청하면 인증에 성공한다")
    @Test
    void should_VerifyEmail_When_RequestVerifyEmailWithCorrectInfo() throws Exception {
        // given
        String email = "unverifiedUser@test.com";
        String code = "12345678";

        // when
        mockMvc.perform(
                        get("/api/users/email-verification")
                                .param("email", email)
                                .param("code", code)
                )
                .andExpect(status().isOk())
                .andDo(document("users-verify-email/success"));
    }

    @DisplayName("올바르지 않은 정보로 이메일 인증을 요청하면 400 에러를 반환한다")
    @Test
    void should_FailToVerifyEmail_When_RequestVerifyEmailWithIncorrectInfo() throws Exception {
        // given
        String email = "unverifiedUser@test.com";
        String code = "wrongCode";

        // when
        mockMvc.perform(
                        get("/api/users/email-verification")
                                .param("email", email)
                                .param("code", code)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("users-verify-email/wrong-code"));
    }

    @DisplayName("가입되지 않은 이메일로 인증을 요청하면 404 에러를 반환한다")
    @Test
    void should_FailToVerifyEmail_When_RequestVerifyEmailWithUnregisteredEmail() throws Exception {
        // given
        String email = "test@test.com";
        String code = "12345678";

        // when
        mockMvc.perform(
                        get("/api/users/email-verification")
                                .param("email", email)
                                .param("code", code)
                )
                .andExpect(status().isNotFound())
                .andDo(document("users-verify-email/not-found-user"));
    }

    @DisplayName("회원가입된 사용자가 비밀번호 찾기 코드를 요청하면 비밀번호 찾기 코드를 전송한다")
    @Test
    void should_SendPasswordRecoveryCode_When_RequestSendPasswordRecoveryCodeWithCorrectInfo() throws Exception {
        // given
        String email = "verifiedUser@test.com";

        String body = objectMapper.writeValueAsString(new SendPasswordRecoveryCodeRequest(email));

        // when
        mockMvc.perform(
                        post("/api/users/password-recovery-code")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andDo(document("users-send-password-recovery-code/success"));
    }

    @DisplayName("가입되지 않은 사용자가 비밀번호 찾기 코드를 요청하면 404 에러를 반환한다")
    @Test
    void should_FailToSendPasswordRecoveryCode_When_RequestSendPasswordRecoveryCodeWithUnregisteredEmail() throws Exception {
        // given
        String email = "test@test.com";


        String body = objectMapper.writeValueAsString(new SendPasswordRecoveryCodeRequest(email));

        // when
        mockMvc.perform(
                        post("/api/users/password-recovery-code")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound())
                .andDo(document("users-send-password-recovery-code/not-found-user"));
    }

    @DisplayName("회원가입된 사용자가 올바른 인증코드로 비밀번호를 재설정 요청하면 비밀번호를 재설정한다")
    @Test
    void should_ResetPassword_When_RequestResetPasswordWithCorrectInfo() throws Exception {
        // given
        String email = "verifiedUser@test.com";
        String verifyCode = "12345678";
        String newPassword = "newPassword";

        String body = objectMapper.writeValueAsString(new ResetPasswordRequest(email, verifyCode, newPassword));

        // when
        mockMvc.perform(
                        post("/api/users/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andDo(document("users-reset-password/success"));
    }

    @DisplayName("회원가입된 사용자가 올바르지 않은 인증코드로 비밀번호를 재설정 요청하면 400 에러를 반환한다")
    @Test
    void should_FailToResetPassword_When_RequestResetPasswordWithIncorrectVerifyCode() throws Exception {
        // given
        String email = "verifiedUser@test.com";
        String verifyCode = "12341234";
        String newPassword = "newPassword";

        String body = objectMapper.writeValueAsString(new ResetPasswordRequest(email, verifyCode, newPassword));

        // when
        mockMvc.perform(
                        post("/api/users/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(document("users-reset-password/wrong-verify-code"));
    }

    @DisplayName("가입되지 않은 사용자가 비밀번호를 재설정 요청하면 404 에러를 반환한다")
    @Test
    void should_FailToResetPassword_When_RequestResetPasswordWithUnregisteredEmail() throws Exception {
        // given
        String email = "test@test.com";
        String verifyCode = "12341234";
        String newPassword = "newPassword";

        String body = objectMapper.writeValueAsString(new ResetPasswordRequest(email, verifyCode, newPassword));

        // when
        mockMvc.perform(
                        post("/api/users/reset-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound())
                .andDo(document("users-reset-password/not-found-user"));
    }

    @DisplayName("사용자가 정보 변경을 요청하면 정보를 변경한다")
    @WithUserDetails(value = "verifiedUser@test.com", userDetailsServiceBeanName = "customUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void should_UpdateUserInfo_When_RequestUpdateUserInfo() throws Exception {
        // given
        String name = "newName";
        String imageUrl = "newImageUrl";

        String body = objectMapper.writeValueAsString(new UpdateUserRequest("newPassword", name, imageUrl));

        // when
        mockMvc.perform(
                        put("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andDo(document("users-update/success"));
    }
}