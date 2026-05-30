package net.nodus.core.site.dto;

import net.nodus.database.site.entity.Site;

import java.util.Objects;

public record SiteResponse(
    String id,
    String name,
    String domain, String url, String siteKey) {

    public SiteResponse(String id, String name, String domain, String url) {
        this(id, name, domain, url, null);
    }

    public static SiteResponse from(Site site) {
        return new SiteResponse(
            Objects.requireNonNull(site.getId()),
            site.getName(),
            site.getDomain(),
            site.getUrl()
        );
    }
}
