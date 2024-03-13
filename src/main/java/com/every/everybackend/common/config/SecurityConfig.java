package com.every.everybackend.common.config;

import com.every.everybackend.common.adapter.JwtAdapter;
import com.every.everybackend.common.filter.JwtAuthenticationFilter;
import com.every.everybackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final JwtAdapter jwtAdapter;
  private final UserRepository userRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http
                                                 ) throws Exception {
    http.authorizeHttpRequests(requests -> requests.requestMatchers("api/users/signup", "api/users/login", "/api/users/email-verification", "/api/users/password-recovery-code", "api/users/reset-password").permitAll()
                    .anyRequest().authenticated()
    )
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(new JwtAuthenticationFilter(userRepository, jwtAdapter), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
          PasswordEncoder passwordEncoder,
          UserDetailsService userDetailsService
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);

    return new ProviderManager(provider);
  }
}
