package net.nodus.core.site.service;

import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteKeyResponse.SiteKeyRecreateResponse;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteKeyService {

    private final UserAccountRepository userAccountRepository;
    private final SiteRepository siteRepository;

    public SiteKeyRecreateResponse recreateKey(String siteId, String userId) {
        Site site = siteRepository.findByIdAndAllowedUserListIdAndDeletedAtIsNull(siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        site.recreateKey();
        Site savedSite = siteRepository.save(site);
        return SiteKeyRecreateResponse.from(savedSite);
    }

}
