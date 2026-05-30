package net.nodus.core.site.dto;

import net.nodus.database.site.SiteKeyStatus;
import net.nodus.database.site.entity.SiteKey;

import java.util.Objects;

public record SiteKeyResponse(String keyId, String keyPrefix, String siteKey, SiteKeyStatus status) {
    public SiteKeyResponse(String keyId, String keyPrefix, SiteKeyStatus status) {
        this(keyId, keyPrefix, null, status);
    }

    public static SiteKeyResponse from(SiteKey siteKey) {
        return new SiteKeyResponse(
                Objects.requireNonNull(siteKey.getId()),
                siteKey.getKeyPrefix(),
                siteKey.getStatus()
        );
    }
}
