package net.nodus.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.nodus.account.OAuthProvider
import net.nodus.account.UserAccount
import net.nodus.account.UserAccountRepository
import net.nodus.auth.service.JwtTokenService
import net.nodus.auth.service.RefreshTokenService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper
import net.nodus.common.exception.GlobalException

@Component
class OAuth2LoginSuccessHandler(
    private val userAccountRepository: UserAccountRepository,
    private val jwtTokenService: JwtTokenService,
    private val refreshTokenService: RefreshTokenService,
    private val objectMapper: ObjectMapper,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oidcUser = authentication.principal as OidcUser

        val user = findOrCreateUser(oidcUser)
        val accessToken = jwtTokenService.createAccessToken(user)
        val refreshToken = refreshTokenService.issue(user)

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        objectMapper.writeValue(
            response.writer,
            OAuthLoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken.token,
            )
        )
    }

    private fun findOrCreateUser(oidcUser: OidcUser): UserAccount {
        val providerId = oidcUser.subject
        val email = oidcUser.email
            ?: throw GlobalException.ExternalApiFailed("Google account email is missing")
        val name = oidcUser.fullName ?: email

        val existingUser = userAccountRepository.findByProviderAndProviderIdAndDeletedAtIsNull(
            provider = OAuthProvider.GOOGLE,
            providerId = providerId
        )

        if (existingUser != null) {
            existingUser.email = email
            existingUser.name = name
            return userAccountRepository.save(existingUser)
        }

        return userAccountRepository.save(
            UserAccount(
                email = email,
                name = name,
                provider = OAuthProvider.GOOGLE,
                providerId = providerId,
            )
        )
    }
}

data class OAuthLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
)
