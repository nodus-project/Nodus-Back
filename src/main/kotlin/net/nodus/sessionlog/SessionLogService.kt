package net.nodus.sessionlog

import net.nodus.site.SiteKeyService
import org.springframework.stereotype.Service

@Service
class SessionLogService(
    private val siteKeyService: SiteKeyService,
    private val sessionLogRepository: SessionLogRepository
) {
    fun create(clientKey: String, userSession: String): SessionLog {
        val siteKey = siteKeyService.authenticate(clientKey)

        return sessionLogRepository.save(
            SessionLog(
                clientKeyId = requireNotNull(siteKey.id),
                siteId = siteKey.siteId,
                userAccountId = siteKey.userAccountId,
                userSession = userSession,
            )
        )
    }
}
