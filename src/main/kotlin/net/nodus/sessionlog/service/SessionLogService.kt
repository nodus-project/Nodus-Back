package net.nodus.sessionlog

import net.nodus.site.SiteKeyService
import org.springframework.stereotype.Service

@Service
class SessionLogService(
    private val clientKeyRepository: ClientKeyRepository,
    private val sessionLogRepository: SessionLogRepository,
) {
    fun create(clientKey: String, userSession: String): SessionLog {
        val key = clientKeyRepository.findByKey(clientKey)
            ?: throw IllegalArgumentException("Client key not found")

        return sessionLogRepository.save(
            SessionLog(
                clientKeyId = requireNotNull(key.id),
                userAccountId = key.userAccountId,
                userSession = userSession,
            )
        )
    }
}