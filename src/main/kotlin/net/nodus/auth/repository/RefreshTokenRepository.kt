package net.nodus.auth.repository

import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun findByTokenHashAndRevokedAtIsNull(tokenHash: String): RefreshToken?
}
