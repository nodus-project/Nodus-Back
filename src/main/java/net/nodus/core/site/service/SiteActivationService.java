package net.nodus.core.site.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationNameCountResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.ActivationResponse;
import net.nodus.core.site.controller.dto.SiteActivationResponse.CountResponse;
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
public class SiteActivationService {

    private final SiteActivationLogRepository siteActivationLogRepository;
    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional(readOnly = true)
    public CountResponse findFirstEventUserCount(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = findAllowedSite(siteId, userId);
        Long count = siteActivationLogRepository.countFirstEventUsers(site, start, end);

        return CountResponse.from(count);
    }

    @Transactional(readOnly = true)
    public List<ActivationNameCountResponse> findActivationNameCounts(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        Site site = findAllowedSite(siteId, userId);

        return siteActivationLogRepository.countByFeatureName(site, start, end).stream()
            .map(ActivationNameCountResponse::from)
            .toList();
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
