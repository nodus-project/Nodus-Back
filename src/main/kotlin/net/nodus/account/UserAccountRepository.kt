package net.nodus.account

import org.springframework.data.mongodb.repository.MongoRepository

interface UserAccountRepository : MongoRepository<UserAccount, String> {
    fun findByProviderAndProviderId(
        provider: OAuthProvider,
        providerId: String,
    ): UserAccount?
}
