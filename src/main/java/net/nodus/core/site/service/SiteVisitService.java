package net.nodus.core.site.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteVisitResponse.SiteVisitCountResponse;
import net.nodus.database.sdk.SiteVisitLog;
import net.nodus.database.sdk.SiteVisitLogRepository;
import net.nodus.database.site.SiteRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteVisitService {

    private final SiteRepository siteRepository;
    private final SiteVisitLogRepository siteVisitLogRepository;

    public SiteVisitCountResponse findVisitCount(
        String siteId,
        LocalDateTime start,
        LocalDateTime end,
        String userId
    ) {
        siteRepository.findByIdAndAllowedUserListIdAndDeletedAtIsNull(siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );

        List<SiteVisitLog> siteVisitLogs = siteVisitLogRepository
            .findAllBySiteIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            siteId,
            start,
            end
        );

        return SiteVisitCountResponse.from(siteVisitLogs);
    }

}
