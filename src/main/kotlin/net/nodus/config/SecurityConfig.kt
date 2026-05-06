package net.nodus.config

import net.nodus.auth.OAuth2LoginSuccessHandler
import net.nodus.security.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
    ) : SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/",
                    "/auth/refresh",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/auth/oauth2/google/code",
                ).permitAll()
                    .requestMatchers("/session-logs/**").authenticated()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                it.successHandler(oAuth2LoginSuccessHandler)
            }
            .headers{
                it.frameOptions { frame -> frame.sameOrigin() }
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter::class.java
            )
            .cors {  }

        return http.build()

    }

    @Bean
    fun corsConfigurationSource(
        @Value("\${app.cors.allowed-origins:http://localhost:3000}") allowedOrigins: String
    ): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = allowedOrigins.split(".").map { it.trim()}
        configuration.allowedMethods = listOf("GET", "POST", "OPTIONS")
        configuration.allowedHeaders = listOf("Authorization", "Content-Type", "X-Requested-With")
        configuration.exposedHeaders = listOf("Authorization", "X-Client-Key")
        configuration.allowCredentials= true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
