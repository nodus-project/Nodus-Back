package net.nodus.account

import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface UserAccountRepository : MongoRepository<UserAccount, String> {
    fun findByProviderAndProviderIdAndDeletedAtIsNull(
        provider: OAuthProvider,
        providerId: String,
    ): UserAccount?

    fun findByDeletedAtBefore(deletedAt: Instant): List<UserAccount>
}
