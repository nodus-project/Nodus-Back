package net.nodus.auth.service

import net.nodus.account.ClientKey
import net.nodus.account.ClientKeyRepository
import net.nodus.account.OAuthProvider
import net.nodus.account.UserAccount
import net.nodus.account.UserAccountRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OAuthLoginService(
    private val userAccountRepository: UserAccountRepository,
    private val clientKeyRepository: ClientKeyRepository,

    private val jwtTokenService: JwtTokenService,
    private val refreshTokenService: RefreshTokenService,
) {

    fun loginGoogleUser(providerId: String, email: String, name: String): OAuthLoginResult {
        val user = userAccountRepository.findByProviderAndProviderId(OAuthProvider.GOOGLE, providerId)
            ?: userAccountRepository.save(
                UserAccount(
                    email = email,
                    name = name,
                    provider = OAuthProvider.GOOGLE,
                    providerId = providerId
                )
            )

        val clientKey = getClientKey(user)
        val refreshToken = refreshTokenService.issue(user)

        return OAuthLoginResult(
            accessToken = jwtTokenService.createAccessToken(user),
            refreshToken = refreshToken.token,
            clientKey = clientKey.key,
        )
    }

    private fun getClientKey(user: UserAccount): ClientKey {
        val userId = requireNotNull(user.id)
        val clientKey = clientKeyRepository.findFirstByUserAccountId(userId)
            ?: clientKeyRepository.save(
                ClientKey(
                    userAccountId = userId,
                    key = generateClientKey(),
                )
            )
        return clientKey
    }

    private fun generateClientKey(): String {
        var key: String
        do {
            key = "ck_" + UUID.randomUUID().toString().replace("-", "")
        } while (clientKeyRepository.existsByKey(key))

        return key
    }
}

data class OAuthLoginResult(
    val accessToken: String,
    val refreshToken: String,
    val clientKey: String,
)