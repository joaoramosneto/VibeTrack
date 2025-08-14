package com.vibetrack.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Importe esta classe
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 1. Configura a API para ser stateless
                .authorizeHttpRequests(authorize -> authorize
                        // 2. Define as regras de permissão
                        .requestMatchers(HttpMethod.POST, "/api/pesquisadores").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/experimentos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pesquisadores/**").permitAll()
                        .anyRequest().authenticated() // 3. Exige autenticação para o resto
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}