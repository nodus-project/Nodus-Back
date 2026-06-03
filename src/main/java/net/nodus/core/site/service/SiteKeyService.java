package net.nodus.core.site.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteKeyResponse.SiteKeyOneResponse;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteAllowedUser;
import net.nodus.database.site.SiteAllowedUserRepository;
import net.nodus.database.site.SiteRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteKeyService {

    private final SiteRepository siteRepository;
    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional
    public SiteKeyOneResponse recreateKey(UUID siteId, UUID userId) {
        SiteAllowedUser allowedSiteList = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        Site site = allowedSiteList.getSite();
        site.recreateKey();
        Site savedSite = siteRepository.save(site);
        return SiteKeyOneResponse.from(savedSite);
    }

}
