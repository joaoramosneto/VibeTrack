package com.vibetrack.backend.config;

import com.vibetrack.backend.Security.SecurityFilter;
import com.vibetrack.backend.users.Service.AuthenticationService; // Import necessário para userDetailsService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Autowired // Adicionado para resolver o erro de compilação no authenticationProvider
    private AuthenticationService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Rota de Login/Registro (Aberto)
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/pesquisadores").permitAll()

                        // Rotas Abertas (Coleta de Dados Mobile)
                        .requestMatchers(HttpMethod.POST, "/api/dados-biometricos/mobile-data").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/mobile/results").permitAll()

                        // Leitura/Download de Mídias (Aberto)
                        .requestMatchers(HttpMethod.GET, "/api/midias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/fotos-perfil/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // VVVV CORREÇÃO: REGRAS ESPECÍFICAS PARA OPERAÇÕES PROTEGIDAS VVVV

                        // 1. Permite DELETE na lista de pesquisadores
                        .requestMatchers(HttpMethod.DELETE, "/api/pesquisadores/*").authenticated()

                        // 2. Permite DELETE na lista de participantes (NOVO)
                        .requestMatchers(HttpMethod.DELETE, "/api/participantes/*").authenticated()

                        // 3. Permite DELETE de mídia individual
                        .requestMatchers(HttpMethod.DELETE, "/api/midias/*").authenticated()

                        // 4. Permite PUT (Upload de Mídia) e POST (Criação de Experimento)
                        .requestMatchers(HttpMethod.PUT, "/api/experimentos/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/experimentos").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/experimentos/*/midia").authenticated()

                        // Qualquer outra requisição deve ser autenticada por padrão
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}