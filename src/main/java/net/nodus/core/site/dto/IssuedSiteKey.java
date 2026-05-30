package net.nodus.core.site.dto;

import net.nodus.database.site.entity.SiteKey;

public record IssuedSiteKey(SiteKey siteKey, String rawKey) {

}
