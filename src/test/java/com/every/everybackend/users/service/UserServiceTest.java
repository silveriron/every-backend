package com.every.everybackend.users.service;

import com.every.everybackend.base.MockTest;
import com.every.everybackend.common.adapter.JwtAdapter;
import com.every.everybackend.common.adapter.MailAdapter;
import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.UserErrorCode;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.entity.enums.UserProvider;
import com.every.everybackend.users.entity.enums.UserRole;
import com.every.everybackend.users.entity.enums.UserStatus;
import com.every.everybackend.users.repository.UserRepository;
import com.every.everybackend.users.service.command.*;
import com.every.everybackend.users.utils.CodeGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest extends MockTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CodeGenerator codeGenerator;
    @Mock
    private MailAdapter mailAdapter;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtAdapter jwtAdapter;
    @Mock
    private Authentication authentication;

    @DisplayName("올바른 사용자 정보를 입력하면 새로운 사용자를 생성하고 인증코드 메일을 발송한다.")
    @Test
    void createUser() {
        // given
        String email = "test@test.com";
        String password = "1234";
        String name = "test";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(password)
                .name(name)
                .status(UserStatus.UNVERIFIED)
                .role(UserRole.USER)
                .provider(UserProvider.EMAIL)
                .verifyCode("12345678")
                .build();

        CreateUserCommand command = new CreateUserCommand(email, password, name, null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(codeGenerator.generateCode(8)).thenReturn("12345678");

        when(passwordEncoder.encode(password)).thenReturn(password);

        // when
        userService.createUser(command);

        // then
        verify(mailAdapter, times(1)).sendMail(any(), any(), any());
    }

    @DisplayName("이미 존재하는 사용자 정보를 입력하면 ApiException이 발생한다.")
    @Test
    void createUserWithExistedUser() {
        // given
        String email = "test@test.com";
        String password = "1234";
        String name = "test";

        CreateUserCommand command = new CreateUserCommand(email, password, name, null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(UserEntity.builder().build()));

        // when
        // then
        assertThrows(ApiException.class, () -> userService.createUser(command));
    }

    @DisplayName("올바른 사용자 정보를 입력하면 로그인을 한다.")
    @Test
    void login() {
        // given
        String email = "test@test.com";
        String password = "1234";

        LoginUserCommand command = new LoginUserCommand(email, password);

        CustomUserDetails userDetails = new CustomUserDetails(UserEntity.builder().email(email).build());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtAdapter.createToken(email)).thenReturn("token");

        // when
        String token = userService.login(command);

        // then
        assertEquals(token, "token");
    }

    @DisplayName("잘못된 사용자 정보로 로그인을 시도하면 에러를 반환한다.")
    @Test
    void loginWithInvalidUser() {
        // given
        String email = "test@test.com";
        String password = "1234";

        LoginUserCommand command = new LoginUserCommand(email, password);

        when(authenticationManager.authenticate(any())).thenThrow(new ApiException(UserErrorCode.INVALID_CREDENTIALS));

        // when
        // then
        assertThrows(ApiException.class, () -> userService.login(command));
    }

    @DisplayName("올바른 이메일과 코드를 입력하면 사용자를 인증한다.")
    @Test
    void verifyEmail() {
        // given
        String email = "test@test.com";
        String code = "12345678";

        EmailVerificationCommand command = new EmailVerificationCommand(email, code);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .status(UserStatus.UNVERIFIED)
                .verifyCode(code)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        userService.verifyEmail(command);

        // then
        verify(userRepository, times(1)).save(any());
    }

    @DisplayName("존재하지 않는 사용자가 인증을 시도하면 ApiException이 발생한다.")
    @Test
    void verifyEmailWithNotExistedUser() {
        // given
        String email = "test@test.com";
        String code = "12345678";

        EmailVerificationCommand command = new EmailVerificationCommand(email, code);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.verifyEmail(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.USER_NOT_FOUND.getCode());
    }

    @DisplayName("이미 인증된 사용자가 인증을 시도하면 ApiException이 발생한다.")
    @Test
    void verifyEmailWithAlreadyVerifiedUser() {
        // given
        String email = "test@test.com";
        String code = "12345678";

        EmailVerificationCommand command = new EmailVerificationCommand(email, code);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .status(UserStatus.ACTIVE)
                .verifyCode(code)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.verifyEmail(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.ALREADY_VERIFIED_USER.getCode());
    }

    @DisplayName("잘못된 코드로 인증을 시도하면 ApiException이 발생한다.")
    @Test
    void verifyEmailWithInvalidCode() {
        // given
        String email = "test@test.com";
        String code = "12345678";

        EmailVerificationCommand command = new EmailVerificationCommand(email, code);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .status(UserStatus.UNVERIFIED)
                .verifyCode("87654321")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.verifyEmail(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.INVALID_CODE.getCode());
    }

    @DisplayName("사용자 정보를 수정한다.")
    @Test
    void updateUser() {
        // given
        String imageUrl = "http://test.com";
        String name = "test";
        String password = "1234";

        UserEntity user = UserEntity.builder()
                .build();

        UpdateUserCommand command = new UpdateUserCommand(user, name, password, imageUrl);

        // when
        userService.updateUser(command);

        // then
        verify(userRepository, times(1)).save(any());
    }

    @DisplayName("비밀번호 찾기 코드를 발송한다.")
    @Test
    void sendPasswordRecoveryCode() {
        // given
        String email = "test@test.com";

        SendPasswordRecoveryCodeCommand command = new SendPasswordRecoveryCodeCommand(email);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(codeGenerator.generateCode(8)).thenReturn("12345678");

        // when
        userService.sendPasswordRecoveryCode(command);

        // then
        verify(userRepository, times(1)).save(any());
        verify(mailAdapter, times(1)).sendMail(any(), any(), any());
    }

    @DisplayName("존재하지 않는 사용자에게 비밀번호 찾기 코드를 발송하면 ApiException이 발생한다.")
    @Test
    void sendPasswordRecoveryCodeWithNotExistedUser() {
        // given
        String email = "test@test.com";

        SendPasswordRecoveryCodeCommand command = new SendPasswordRecoveryCodeCommand(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.sendPasswordRecoveryCode(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.USER_NOT_FOUND.getCode());
    }

    @DisplayName("인증 코드가 일치하면 비밀번호를 변경한다.")
    @Test
    void resetPassword() {
        // given
        String email = "test@test.com";
        String code = "12345678";
        String password = "1234";

        ResetPasswordCommand command = new ResetPasswordCommand(email, code, password);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .verifyCode(code)
                .build();

        when(passwordEncoder.encode(password)).thenReturn(password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        userService.resetPassword(command);

        // then
        verify(userRepository, times(1)).save(any());
    }

    @DisplayName("존재하지 않는 사용자의 비밀번호를 변경하려고 하면 ApiException이 발생한다.")
    @Test
    void resetPasswordWithNotExistedUser() {
        // given
        String email = "test@test.com";
        String code = "12345678";
        String password = "1234";

        ResetPasswordCommand command = new ResetPasswordCommand(email, code, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.resetPassword(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.USER_NOT_FOUND.getCode());
    }

    @DisplayName("인증 코드가 일치하지 않으면 ApiException이 발생한다.")
    @Test
    void resetPasswordWithInvalidCode() {
        // given
        String email = "test@test.com";
        String code = "12345678";
        String password = "1234";

        ResetPasswordCommand command = new ResetPasswordCommand(email, code, password);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .verifyCode("87654321")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.resetPassword(command));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.INVALID_CODE.getCode());
    }

    @DisplayName("사용자 아이디로 사용자 정보를 조회한다")
    @Test
    void loadUserByUsername() {
        // given
        Long userId = 1L;
        String email = "test@test.com";

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .build();

        userEntity.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // when
        UserEntity user = userService.findById(userId);

        // then
        assertEquals(user.getId(), userId);
        assertEquals(user.getEmail(), email);
    }

    @DisplayName("사용자 아이디로 사용자 정보를 조회할 때 사용자가 존재하지 않으면 ApiException이 발생한다.")
    @Test
    void loadUserByUsernameWithNotExistedUser() {
        // given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        ApiException apiException = assertThrows(ApiException.class, () -> userService.findById(userId));

        // then
        assertEquals(apiException.getErrorResponse().getCode(), UserErrorCode.USER_NOT_FOUND.getCode());
    }
}
