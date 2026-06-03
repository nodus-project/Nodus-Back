package net.nodus.core.sdk.service;

import lombok.RequiredArgsConstructor;
import net.nodus.core.sdk.controller.dto.SdkLogRequest.SdkActivationPostRequest;
import net.nodus.core.sdk.controller.dto.SdkLogRequest.SdkVisitPostRequest;
import net.nodus.database.sdk.SiteActivationLog;
import net.nodus.database.sdk.SiteVisitLog;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SdkLogService {

    private final SiteRepository siteRepository;

    @Transactional
    public void visitLog(String key, SdkVisitPostRequest dto) {
        Site site = siteRepository.findByKey(key)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );

        SiteVisitLog visitLog = SiteVisitLog.builder()
            .sessionId(dto.sessionId())
            .lastPage(dto.lastPage())
            .currentPage(dto.currentPage())
            .build();

        site.addVisitLog(visitLog);
    }

    @Transactional
    public void activationLog(String key, SdkActivationPostRequest dto) {
        Site site = siteRepository.findByKey(key)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );

        SiteActivationLog activationLog = SiteActivationLog.builder()
            .sessionId(dto.sessionId())
            .featureName(dto.featureName())
            .currentPage(dto.currentPage())
            .build();

        site.addActivationLog(activationLog);
    }
}
