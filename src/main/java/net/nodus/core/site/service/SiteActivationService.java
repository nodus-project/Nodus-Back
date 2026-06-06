package net.nodus.core.site.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationRateResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActiveUserCountResponse;
import net.nodus.database.sdk.SiteActivationLog;
import net.nodus.database.sdk.SiteActivationLogRepository;
import net.nodus.database.sdk.SiteVisitLog;
import net.nodus.database.sdk.SiteVisitLogRepository;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteAllowedUser;
import net.nodus.database.site.SiteAllowedUserRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteActivationService {

    private final SiteVisitLogRepository siteVisitLogRepository;
    private final SiteActivationLogRepository siteActivationLogRepository;
    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional(readOnly = true)
    public ActiveUserCountResponse findActiveUserLogs(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = getAllowedSite(siteId, userId);
        List<SiteVisitLog> visitLogList = siteVisitLogRepository.findVisitLogs(
            site,
            start,
            end
        );
        return ActiveUserCountResponse.from(visitLogList);
    }

    @Transactional(readOnly = true)
    public ActivationRateResponse findActivationRateLogs(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = getAllowedSite(siteId, userId);
        List<SiteVisitLog> visitLogList = siteVisitLogRepository.findVisitLogs(site, start, end);
        List<SiteActivationLog> activationLogList = siteActivationLogRepository.findActivationLogs(
            site, start, end);
        return ActivationRateResponse.from(visitLogList, activationLogList);
    }

    @Transactional(readOnly = true)
    public TimeToActivationLogs findTimeToActivationLogs(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = getAllowedSite(siteId, userId);
        return new TimeToActivationLogs(
            siteVisitLogRepository.findVisitLogs(site, start, end),
            siteActivationLogRepository.findActivationLogs(site, start, end)
        );
    }

    @Transactional(readOnly = true)
    public FirstEventLogs findFirstEventLogs(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = getAllowedSite(siteId, userId);
        return new FirstEventLogs(
            siteVisitLogRepository.findVisitLogs(site, start, end),
            siteActivationLogRepository.findActivationLogs(site, start, end)
        );
    }

    private Site getAllowedSite(UUID siteId, UUID userId) {
        SiteAllowedUser allowedSite = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        return allowedSite.getSite();
    }

    public record ActivationRateLogs(
        List<SiteVisitLog> visitLogs,
        List<SiteActivationLog> activationLogs
    ) {

    }

    public record TimeToActivationLogs(
        List<SiteVisitLog> visitLogs,
        List<SiteActivationLog> activationLogs
    ) {

    }

    public record FirstEventLogs(
        List<SiteVisitLog> visitLogs,
        List<SiteActivationLog> activationLogs
    ) {

    }
}
