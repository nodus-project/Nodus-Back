package net.nodus.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import net.nodus.account.UserAccount
import net.nodus.config.annotation.Facade
import org.springframework.beans.factory.annotation.Value
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Facade
class JwtTokenService(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.access-token-expiration-seconds}")
    private val expirationSeconds: Long,
) {

    private val signingKey: SecretKey
        get() = Keys.hmacShaKeyFor(secret.toByteArray())

    fun createAccessToken(user: UserAccount): String {
        val userId = requireNotNull(user.id)
        val now = Instant.now()
        val expiresAt = now.plusSeconds(expirationSeconds)

        return Jwts.builder()
            .subject(userId)
            .claim("email", user.email)
            .claim("name", user.name)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(signingKey, Jwts.SIG.HS256)
            .compact()
    }

    fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload

}