package net.nodus.config

import net.nodus.auth.OAuth2LoginSuccessHandler
import net.nodus.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
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
}
