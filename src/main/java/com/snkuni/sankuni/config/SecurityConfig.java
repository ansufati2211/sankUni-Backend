package com.snkuni.sankuni.config;

import com.snkuni.sankuni.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    
    // Eliminamos la variable del AuthenticationProvider que estaba aqui

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
    "/api/v1/auth/**",
    "/v3/api-docs",    // Agrega esta línea específica
    "/v3/api-docs/**", 
    "/swagger-ui/**", 
    "/swagger-ui.html"
).permitAll()
                    .anyRequest().authenticated() 
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Eliminamos la linea .authenticationProvider() de aqui
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al configurar la cadena de filtros de seguridad", ex);
        }
    }
}