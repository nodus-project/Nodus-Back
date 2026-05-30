package net.nodus.core.site.dto;

import net.nodus.database.site.entity.Site;

public record SiteCreateResult(Site site, IssuedSiteKey issuedKey) {

}
