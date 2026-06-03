package net.nodus.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain sdkSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/sdk/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                .anyRequest().permitAll()
            )
            .cors(cors -> {
            });

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(
                    "/",
                    "/auth/refresh",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/auth/oauth2/google/code"
                ).permitAll()
                .requestMatchers("/session-logs/**").permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .cors(cors -> {
            });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/sdk/**", sdkCorsConfiguration());
        source.registerCorsConfiguration("/**", defaultCorsConfiguration());
        return source;
    }

    private CorsConfiguration sdkCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(
            List.of(
                "POST"
            )
        );
        configuration.setAllowedHeaders(List.of());
        configuration.setAllowCredentials(false);
        return configuration;
    }

    private CorsConfiguration defaultCorsConfiguration() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            List.of(
                "http://localhost:3000",
                "https://nodus.ministudiolab.com"
            )
        );

        configuration.setAllowedMethods(
            List.of(
                "GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH"
            )
        );

        configuration.setAllowedHeaders(
            List.of(
                "Authorization", "Content-Type", "X-Requested-With"
            )
        );

        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        return configuration;
    }
}
