package com.snkuni.sankuni.config;

import com.snkuni.sankuni.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                    // 1. Rutas Públicas (Auth y Documentación)
                    .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    
                    // 2. Rutas Públicas para la Landing Page (Solo de lectura)
                    .requestMatchers(HttpMethod.GET, "/api/v1/carreras", "/api/v1/cursos").permitAll()
                    
                    // 3. Rutas Públicas para Formularios Web (Solo envío de datos)
                    .requestMatchers(HttpMethod.POST, "/api/v1/mensajes", "/api/v1/postulantes").permitAll()
                    
                  
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception e) {
            throw new IllegalStateException("Error en la configuración de seguridad", e);
        }
    }
}