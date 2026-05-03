package net.nodus.auth

import net.nodus.account.UserAccount
import net.nodus.account.UserAccountRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userAccountRepository: UserAccountRepository,

    @Value("\${jwt.refresh-token-expiration-seconds}")
    private val refreshTokenExpirationSeconds: Long,
) {
    private val secureRandom = SecureRandom()

    fun issue(user: UserAccount): IssuedRefreshToken {
        val token = generateToken()
        val tokenHash = hash(token)

        val refreshToken = refreshTokenRepository.save(
            RefreshToken(
                userAccountId = requireNotNull(user.id),
                tokenHash = tokenHash,
                expiresAt = Instant.now().plusSeconds(refreshTokenExpirationSeconds),
            )
        )

        return IssuedRefreshToken(
            token = token,
            userAccount = user,
            refreshToken = refreshToken,
        )
    }

    fun rotate(rawToken: String): IssuedRefreshToken {
        val now = Instant.now()
        val refreshToken = refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(hash(rawToken))
            ?: throw IllegalArgumentException("Invalid refresh token")

        if (refreshToken.expiresAt.isBefore(now)) {
            refreshToken.revokedAt = now
            refreshTokenRepository.save(refreshToken)
            throw IllegalArgumentException("Expired refresh token")
        }

        val user = userAccountRepository.findByIdOrNull(refreshToken.userAccountId)
            ?: throw IllegalArgumentException("Refresh token user not found")

        refreshToken.revokedAt = now
        refreshTokenRepository.save(refreshToken)

        return issue(user)
    }

    private fun generateToken(): String {
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun hash(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(token.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}

data class IssuedRefreshToken(
    val token: String,
    val userAccount: UserAccount,
    val refreshToken: RefreshToken,
)
