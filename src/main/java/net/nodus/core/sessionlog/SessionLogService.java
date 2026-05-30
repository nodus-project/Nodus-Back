package net.nodus.core.sessionlog;

import java.util.Objects;
import net.nodus.core.site.SiteKeyService;
import net.nodus.database.sessionlog.SessionLog;
import net.nodus.database.sessionlog.SessionLogRepository;
import net.nodus.database.site.entity.SiteKey;
import org.springframework.stereotype.Service;

@Service
public class SessionLogService {

    private final SiteKeyService siteKeyService;
    private final SessionLogRepository sessionLogRepository;

    public SessionLogService(SiteKeyService siteKeyService,
        SessionLogRepository sessionLogRepository) {
        this.siteKeyService = siteKeyService;
        this.sessionLogRepository = sessionLogRepository;
    }

    public SessionLog create(String clientKey, String userSession) {
        SiteKey siteKey = siteKeyService.authenticate(clientKey);

        return sessionLogRepository.save(new SessionLog(
            Objects.requireNonNull(siteKey.getId()),
            siteKey.getSiteId(),
            siteKey.getUserAccountId(),
            userSession
        ));
    }
}
