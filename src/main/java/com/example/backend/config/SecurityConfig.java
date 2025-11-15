package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // desativa CSRF (pra facilitar testes)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()
            )
            .cors(cors -> {}); // ativa seu CorsConfig
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa o algoritmo BCrypt (padrão recomendado)
        return new BCryptPasswordEncoder();
    }
}
