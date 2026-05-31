package net.nodus.core.sdk.service;

import lombok.RequiredArgsConstructor;
import net.nodus.core.sdk.controller.dto.SdkVisitRequest.SdkVisitPostRequest;
import net.nodus.database.sdk.SiteVisitLog;
import net.nodus.database.sdk.SiteVisitLogRepository;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SdkVisitService {

    private final SiteRepository siteRepository;
    private final SiteVisitLogRepository siteVisitLogRepository;

    public void create(String key, SdkVisitPostRequest dto) {
        Site site = siteRepository.findByKeyAndDeletedAtIsNull(key)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );

        SiteVisitLog siteVisitLog = SiteVisitLog.builder()
            .siteId(site.getId())
            .sessionId(dto.sessionId())
            .lastPage(dto.lastPage())
            .currentPage(dto.currentPage())
            .build();

        siteVisitLogRepository.save(siteVisitLog);
    }
}
