package net.nodus.config

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DeletedDataCleanupScheduler(
    private val cleanupService: DeletedDataCleanupService,
) {
    @Scheduled(cron = "0 0 3 * * *")
    fun cleanup() {
        cleanupService.hardDeleteExpiredData()
    }
}