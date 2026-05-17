package net.nodus.config

import net.nodus.account.UserAccountRepository
import net.nodus.auth.RefreshTokenRepository
import net.nodus.site.SiteKeyRepository
import net.nodus.site.SiteRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class DeletedDataCleanupService(
    private val siteRepository: SiteRepository,
    private val siteKeyRepository: SiteKeyRepository,
    private val userAccountRepository: UserAccountRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun hardDeleteExpiredData() {
        val threshold = Instant.now().minus(365, ChronoUnit.DAYS)

        siteRepository.deleteAll(siteRepository.findByDeletedAtBefore(threshold))
        siteKeyRepository.deleteAll(siteKeyRepository.findByDeletedAtBefore(threshold))
        userAccountRepository.deleteAll(userAccountRepository.findByDeletedAtBefore(threshold))
        refreshTokenRepository.deleteAll(refreshTokenRepository.findByDeletedAtBefore(threshold))
    }
}