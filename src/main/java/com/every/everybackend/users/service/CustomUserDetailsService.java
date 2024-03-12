package com.every.everybackend.users.service;

import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.repository.UserRepository;
import com.every.everybackend.users.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> existUser = userRepository.findByEmail(email);

        if (existUser.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }

        return new CustomUserDetails(existUser.get());
    }
}
