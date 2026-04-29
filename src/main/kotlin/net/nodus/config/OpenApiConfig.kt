package net.nodus.config


import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Nodus API")
                    .version("v1")
                    .description(
                        """
                            Authentication:
                            - Google OAuth2 login: GET /oauth2/authorization/google
                            - OAuth2 callback: GET/login/oauth2/code/google
                            - API authentication: Authorization: Bearer <accessToken>
                            
                            Session log ingestionL
                            - POST /session-logs
                            - Currently accepts clientKey in request body.
                        """.trimIndent()
                    )
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerToken",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
            .addSecurityItem(
                SecurityRequirement().addList("bearerAuth")
            )
}