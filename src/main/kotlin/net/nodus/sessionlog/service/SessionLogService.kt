package net.nodus.sessionlog

import net.nodus.account.ClientKeyRepository
import net.nodus.sessionlog.repository.SessionLog
import net.nodus.sessionlog.repository.SessionLogRepository
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
                siteId = "mock",
                userAccountId = key.userAccountId,
                userSession = userSession,
            )
        )
    }
}