package net.nodus.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.nodus.account.ClientKey
import net.nodus.account.ClientKeyRepository
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
import java.lang.IllegalStateException
import java.util.UUID

@Component
class OAuth2LoginSuccessHandler(
    private val userAccountRepository: UserAccountRepository,
    private val clientKeyRepository: ClientKeyRepository,
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
        val clientKey = findOrCreateClientKey(user)
        val accessToken = jwtTokenService.createAccessToken(user)
        val refreshToken = refreshTokenService.issue(user)

        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        objectMapper.writeValue(
            response.writer,
            OAuthLoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken.token,
                clientKey = clientKey.key
            )
        )
    }

    private fun findOrCreateUser(oidcUser: OidcUser): UserAccount {
        val providerId = oidcUser.subject
        val email = oidcUser.email
            ?: throw IllegalStateException("Google account email is missing")
        val name = oidcUser.fullName ?: email

        val existingUser = userAccountRepository.findByProviderAndProviderId(
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

    private fun findOrCreateClientKey(user: UserAccount): ClientKey {
        val userId = requireNotNull(user.id)

        val existingClientKey = clientKeyRepository.findFirstByUserAccountId(userId)

        if(existingClientKey != null) {
            return existingClientKey
        }

        var key: String

        do {
            key = "ck_" + UUID.randomUUID().toString().replace("-", "")
        } while (clientKeyRepository.existsByKey(key))

        return clientKeyRepository.save(
            ClientKey(
                userAccountId = userId,
                key = key
            )
        )
    }
}

data class OAuthLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val clientKey: String,
)
