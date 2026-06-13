package net.nodus.core.site.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteSummaryResponse.ActivationResponse;
import net.nodus.core.site.controller.dto.SiteSummaryResponse.TrafficSourceSummaryResponse;
import net.nodus.database.sdk.SiteActivationLogRepository;
import net.nodus.database.sdk.SiteRevenueLogRepository;
import net.nodus.database.sdk.SiteTrafficSource;
import net.nodus.database.sdk.SiteTrafficSourceRepository;
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
    private final SiteTrafficSourceRepository siteTrafficSourceRepository;

    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional(readOnly = true)
    public ActivationResponse findLogList(
        UUID siteId,
        UUID userId
    ) {
        Site site = findAllowedSite(siteId, userId);

        Long acquisitionCount = siteVisitLogRepository.countDistinctSessionId(site);
        Long activationCount = siteActivationLogRepository.countDistinctSessionId(site);
        Long revenueCount = siteRevenueLogRepository.countDistinctSessionId(site);
        Long retentionCount = siteVisitLogRepository.countRetentionSessionId(site);

        return ActivationResponse.from(
            acquisitionCount,
            activationCount,
            revenueCount,
            retentionCount
        );
    }

    @Transactional(readOnly = true)
    public List<TrafficSourceSummaryResponse> findTrafficSourceSummary(
        UUID siteId,
        UUID userId
    ) {
        Site site = findAllowedSite(siteId, userId);
        List<SiteTrafficSource> trafficSourceList = siteTrafficSourceRepository.findAllBySite(site);
        return TrafficSourceSummaryResponse.from(trafficSourceList);
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
