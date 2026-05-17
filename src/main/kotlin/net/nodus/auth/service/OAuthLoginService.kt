package net.nodus.auth.service

import net.nodus.account.OAuthProvider
import net.nodus.account.UserAccount
import net.nodus.account.UserAccountRepository
import org.springframework.stereotype.Service

@Service
class OAuthLoginService(
    private val userAccountRepository: UserAccountRepository,
    private val jwtTokenService: JwtTokenService,
    private val refreshTokenService: RefreshTokenService,
) {

    fun loginGoogleUser(providerId: String, email: String, name: String): OAuthLoginResult {
        val user = userAccountRepository.findByProviderAndProviderIdAndDeletedAtIsNull(OAuthProvider.GOOGLE, providerId)
            ?: userAccountRepository.save(
                UserAccount(
                    email = email,
                    name = name,
                    provider = OAuthProvider.GOOGLE,
                    providerId = providerId
                )
            )
        val refreshToken = refreshTokenService.issue(user)

        return OAuthLoginResult(
            accessToken = jwtTokenService.createAccessToken(user),
            refreshToken = refreshToken.token,
        )
    }
}

data class OAuthLoginResult(
    val accessToken: String,
    val refreshToken: String,
)