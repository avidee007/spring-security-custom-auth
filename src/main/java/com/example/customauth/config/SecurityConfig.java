package com.example.customauth.config;

import com.example.customauth.service.AesTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {
  private final AesTokenService tokenService;

  public SecurityConfig(AesTokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(
            authorize-> {
              authorize.requestMatchers("/token/**").permitAll();
              authorize.anyRequest().authenticated();
            }
        )
        .addFilterBefore(new CustomAuthFilter(tokenService), AuthorizationFilter.class)
        .build();
  }
}
