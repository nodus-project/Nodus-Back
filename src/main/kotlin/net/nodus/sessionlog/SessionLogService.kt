package net.nodus.sessionlog

import jakarta.transaction.Transactional
import net.nodus.account.ClientKeyRepository
import org.springframework.stereotype.Service

@Service
class SessionLogService(
    private val clientKeyRepository: ClientKeyRepository,
    private val sessionLogRepository: SessionLogRepository,
) {
    @Transactional
    fun create(clientKey: String, userSession: String): SessionLog {
        val key = clientKeyRepository.findByKey(clientKey)
            ?: throw IllegalArgumentException("Client key not found")

        return sessionLogRepository.save(
            SessionLog(
                clientKey = key,
                userSession = userSession,
            )
        )
    }
}

// dashboard/uuid
// /dashboard  <- 검증을 해서 이  url만 들어오도록 하면 되지