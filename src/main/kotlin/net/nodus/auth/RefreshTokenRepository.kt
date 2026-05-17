package net.nodus.auth

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(tokenHash: String): RefreshToken?

    fun findByDeletedAtBefore(deletedAt: Instant): List<RefreshToken>
}
