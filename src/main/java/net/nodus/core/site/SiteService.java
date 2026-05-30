package net.nodus.core.site;

import java.util.List;
import net.nodus.global.common.exception.GlobalException;
import net.nodus.core.site.dto.CreateSiteRequest;
import net.nodus.core.site.dto.IssuedSiteKey;
import net.nodus.core.site.dto.SiteCreateResult;
import net.nodus.core.site.dto.UpdateSiteRequest;
import net.nodus.database.site.SiteKeyRepository;
import net.nodus.database.site.SiteKeyStatus;
import net.nodus.database.site.SiteRepository;
import net.nodus.database.site.entity.Site;
import net.nodus.database.site.entity.SiteKey;
import org.springframework.stereotype.Service;

@Service
public class SiteService {

    private final SiteRepository siteRepository;
    private final SiteKeyRepository siteKeyRepository;
    private final SiteKeyService siteKeyService;

    public SiteService(SiteRepository siteRepository, SiteKeyRepository siteKeyRepository,
        SiteKeyService siteKeyService) {
        this.siteRepository = siteRepository;
        this.siteKeyRepository = siteKeyRepository;
        this.siteKeyService = siteKeyService;
    }

    public SiteCreateResult create(String userId, CreateSiteRequest request) {
        Site site = siteRepository.save(
            new Site(userId, request.getName(), request.getDomain(), request.getUrl()));
        return new SiteCreateResult(site, siteKeyService.issue(site));
    }

    public List<Site> list(String userId) {
        return siteRepository.findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(userId);
    }

    public Site get(String userId, String siteId) {
        return siteRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(siteId, userId)
            .orElseThrow(() -> new GlobalException.DataNotFound("Site not found"));
    }

    public Site update(String userId, String siteId, UpdateSiteRequest request) {
        Site site = get(userId, siteId);

        if (request.getName() != null) {
            site.setName(request.getName());
        }
        site.setDomain(request.getDomain());
        site.setUrl(request.getUrl());
        site.touch();

        return siteRepository.save(site);
    }

    public void delete(String userId, String siteId) {
        Site site = get(userId, siteId);

        site.softDelete();

        siteRepository.save(site);
        siteKeyService.revokeActive(siteId);
    }

    public SiteKey keyInfo(String userId, String siteId) {
        get(userId, siteId);

        return siteKeyRepository.findBySiteIdAndStatusAndDeletedAtIsNull(siteId,
                SiteKeyStatus.ACTIVE)
            .orElseThrow(() -> new GlobalException.DataNotFound("Active site key not found"));
    }

    public IssuedSiteKey rotateKey(String userId, String siteId) {
        Site site = get(userId, siteId);
        return siteKeyService.issue(site);
    }

    public void revokeKey(String userId, String siteId) {
        get(userId, siteId);
        siteKeyService.revokeActive(siteId);
    }
}
