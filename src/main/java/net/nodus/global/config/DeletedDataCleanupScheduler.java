package net.nodus.global.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeletedDataCleanupScheduler {

    private final DeletedDataCleanupService cleanupService;

    public DeletedDataCleanupScheduler(DeletedDataCleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {
        cleanupService.hardDeleteExpiredData();
    }
}
