package net.nodus.core.site.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteRequest.SiteCreateRequest;
import net.nodus.core.site.controller.dto.SiteRequest.UpdateSiteRequest;
import net.nodus.core.site.controller.dto.SiteResponse.SiteOneResponse;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.database.site.Site;
import net.nodus.database.site.SiteAllowedUser;
import net.nodus.database.site.SiteAllowedUserRepository;
import net.nodus.database.site.SiteRepository;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteService {

    private final UserAccountRepository userAccountRepository;
    private final SiteRepository siteRepository;
    private final SiteAllowedUserRepository siteAllowedUserRepository;

    @Transactional
    public SiteOneResponse create(SiteCreateRequest dto, UUID userId) {
        UserAccount userAccount = userAccountRepository.findById(userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.USER_NOT_FOUND)
            );

        Site site = Site.builder()
            .name(dto.name())
            .url(dto.url())
            .build();

        SiteAllowedUser allowedUser = SiteAllowedUser.builder()
            .userAccount(userAccount)
            .build();

        site.addAllowedUser(allowedUser);
        Site savedSite = siteRepository.save(site);

        return SiteOneResponse.from(savedSite);
    }

    @Transactional(readOnly = true)
    public List<SiteOneResponse> getSiteList(UUID userId) {
        List<SiteAllowedUser> allowedSiteList = siteAllowedUserRepository.findByUserAccountId(
            userId);
        return allowedSiteList.stream().map(
            allowedSite -> SiteOneResponse.from(allowedSite.getSite())
        ).toList();
    }


    @Transactional(readOnly = true)
    public SiteOneResponse getSite(UUID siteId, UUID userId) {
        SiteAllowedUser allowedSiteList = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        Site site = allowedSiteList.getSite();
        return SiteOneResponse.from(site);
    }

    @Transactional
    public SiteOneResponse updateSite(UUID siteId, UpdateSiteRequest dto, UUID userId) {
        SiteAllowedUser allowedSiteList = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        Site site = allowedSiteList.getSite();
        site.updateBaseInfo(dto.name(), dto.url());
        Site savedSite = siteRepository.save(site);
        return SiteOneResponse.from(savedSite);
    }

    @Transactional
    public void deleteSite(UUID siteId, UUID userId) {
        SiteAllowedUser allowedSiteList = siteAllowedUserRepository.findBySiteIdAndUserAccountId(
                siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        Site site = allowedSiteList.getSite();
        siteRepository.delete(site);
    }

}
