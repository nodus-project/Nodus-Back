package net.nodus.core.site.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteVisitResponse.SiteVisitCountResponse;
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
public class SiteVisitService {

    private final SiteVisitLogRepository siteVisitLogRepository;
    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional(readOnly = true)
    public SiteVisitCountResponse findVisitCount(
        UUID siteId,
        LocalDateTime start,
        LocalDateTime end,
        UUID userId
    ) {
        SiteAllowedUser allowedSiteList = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        Site site = allowedSiteList.getSite();
        List<SiteVisitLog> visitLogList = siteVisitLogRepository.findLogList(site, start, end);

        return SiteVisitCountResponse.from(visitLogList);
    }

}
