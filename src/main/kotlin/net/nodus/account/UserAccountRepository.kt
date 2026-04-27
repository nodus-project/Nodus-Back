package net.nodus.account

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserAccountRepository : JpaRepository<UserAccount, UUID> {
    fun findByProviderAndProviderId(
        provider: OAuthProvider,
        providerId: String,
    ): UserAccount?

}