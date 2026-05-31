package net.nodus.core.site.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nodus.core.site.controller.dto.SiteBaseRequest.SiteCreateRequest;
import net.nodus.core.site.controller.dto.SiteBaseRequest.UpdateSiteRequest;
import net.nodus.core.site.controller.dto.SiteBaseResponse.SiteOneResponse;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.database.site.SiteRepository;
import net.nodus.database.site.Site;
import net.nodus.global.common.exception.DataNotFound;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteBaseService {

    private final UserAccountRepository userAccountRepository;
    private final SiteRepository siteRepository;

    public SiteOneResponse create(SiteCreateRequest dto, String userId) {
        UserAccount userAccount = userAccountRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.USER_NOT_FOUND)
            );

        Site site = Site.builder()
            .name(dto.name())
            .url(dto.url())
            .build();

        site.addAllowedUser(userAccount);
        Site savedSite = siteRepository.save(site);

        return SiteOneResponse.from(savedSite);
    }

    public List<SiteOneResponse> getSiteList(String userId) {
        List<Site> siteList = siteRepository.findAllByAllowedUserListIdAndDeletedAtIsNull(userId);
        return siteList.stream().map(SiteOneResponse::from).toList();
    }

    public SiteOneResponse getSite(String siteId, String userId) {
        Site site = siteRepository.findByIdAndAllowedUserListIdAndDeletedAtIsNull(siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        return SiteOneResponse.from(site);
    }

    public SiteOneResponse updateSite(String siteId, UpdateSiteRequest dto, String userId) {
        Site site = siteRepository.findByIdAndAllowedUserListIdAndDeletedAtIsNull(siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );
        site.update(dto.name(), dto.url());
        Site savedSite = siteRepository.save(site);
        return SiteOneResponse.from(savedSite);
    }

    public void deleteSite(String siteId, String userId) {
        Site site = siteRepository.findByIdAndAllowedUserListIdAndDeletedAtIsNull(siteId, userId)
            .orElseThrow(
                () -> new DataNotFound(DataNotFound.SITE_NOT_FOUND)
            );

        site.delete();
        siteRepository.save(site);
    }

}
