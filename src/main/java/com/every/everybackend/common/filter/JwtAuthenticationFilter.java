package com.every.everybackend.common.filter;

import com.every.everybackend.common.adapter.JwtAdapter;
import com.every.everybackend.common.exception.ApiException;
import com.every.everybackend.common.exception.errorcode.AuthErrorCode;
import com.every.everybackend.users.domain.CustomUserDetails;
import com.every.everybackend.users.entity.UserEntity;
import com.every.everybackend.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtAdapter jwtAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null) {

            String email = jwtAdapter.getEmail(token);

            UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.INVALID_TOKEN));

            UserDetails userDetails = new CustomUserDetails(userEntity);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}