package com.nexteam.security.config;

import com.nexteam.security.error401.JwtAuthenticationEntryPoint;
import com.nexteam.security.error403.JwtAccessDeniedHandler;
import com.nexteam.security.jwt.JwtAuthFilter;
import com.nexteam.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAccessDeniedHandler accessDeniedHandler; // 403
    private final JwtAuthenticationEntryPoint authenticationEntryPoint; // 401
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService, userDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://nexteam.com",
                "http://localhost:5500",
                "http://10.50.101.16:4200/",
                "http://169.254.190.148:4200/",
                "http://169.254.57.215:4200/"
        ));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("Content-Type"));

        config.setAllowCredentials(true);

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Routes publiques (pas d'authentification requise)
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/logout").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/ws/**").permitAll() // handshake WebSocket, sécurisé séparément par JwtHandshakeInterceptor

                        // Routes réservées à un rôle précis
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/news").hasAnyRole("ADMIN", "RH")

                        // Tout le reste nécessite d'être authentifié, peu importe le rôle
                        .anyRequest().authenticated())
                // accessDeniedHandler → quand un utilisateur connecté tente d’accéder à une ressource pour laquelle il n’a pas le droit.
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler)
                        // authenticationEntryPoint → quand un utilisateur non connecté tente d’accéder à une ressource sécurisée.
                        .authenticationEntryPoint(authenticationEntryPoint))
                // Configure la gestion de session.
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                // Add JWT filter before the Spring Security filter that handles form authentication
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
