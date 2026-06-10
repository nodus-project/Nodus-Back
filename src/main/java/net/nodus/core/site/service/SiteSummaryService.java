package net.nodus.core.site.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationResponse;
import net.nodus.database.sdk.SiteActivationLogRepository;
import net.nodus.database.sdk.SiteRevenueLogRepository;
import net.nodus.database.sdk.SiteVisitLogRepository;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteAllowedUser;
import net.nodus.database.site.SiteAllowedUserRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteSummaryService {

    private final SiteVisitLogRepository siteVisitLogRepository;
    private final SiteActivationLogRepository siteActivationLogRepository;
    private final SiteRevenueLogRepository siteRevenueLogRepository;
    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional(readOnly = true)
    public ActivationResponse findLogList(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = findAllowedSite(siteId, userId);

        Long visitCount = siteVisitLogRepository.countDistinctSessionId(site, start, end);
        Long activationCount = siteActivationLogRepository.countDistinctSessionId(site, start, end);
        Long revenueCount = siteRevenueLogRepository.countDistinctSessionId(site, start, end);

        return ActivationResponse.from(
            activationCount,
            visitCount,
            revenueCount
        );
    }

    private Site findAllowedSite(UUID siteId, UUID userId) {
        SiteAllowedUser allowedSite = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );

        return allowedSite.getSite();
    }

}
