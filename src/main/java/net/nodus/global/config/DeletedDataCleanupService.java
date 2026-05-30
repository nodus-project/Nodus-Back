package net.nodus.global.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.database.auth.RefreshTokenRepository;
import net.nodus.database.site.SiteKeyRepository;
import net.nodus.database.site.SiteRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletedDataCleanupService {

    private final SiteRepository siteRepository;
    private final SiteKeyRepository siteKeyRepository;
    private final UserAccountRepository userAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public DeletedDataCleanupService(
        SiteRepository siteRepository,
        SiteKeyRepository siteKeyRepository,
        UserAccountRepository userAccountRepository,
        RefreshTokenRepository refreshTokenRepository
    ) {
        this.siteRepository = siteRepository;
        this.siteKeyRepository = siteKeyRepository;
        this.userAccountRepository = userAccountRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void hardDeleteExpiredData() {
        Instant threshold = Instant.now().minus(365, ChronoUnit.DAYS);

        siteRepository.deleteAll(siteRepository.findByDeletedAtBefore(threshold));
        siteKeyRepository.deleteAll(siteKeyRepository.findByDeletedAtBefore(threshold));
        userAccountRepository.deleteAll(userAccountRepository.findByDeletedAtBefore(threshold));
        refreshTokenRepository.deleteAll(refreshTokenRepository.findByDeletedAtBefore(threshold));
    }
}
