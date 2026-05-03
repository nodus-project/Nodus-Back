package net.nodus.auth

import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun findByTokenHashAndRevokedAtIsNull(tokenHash: String): RefreshToken?
}
