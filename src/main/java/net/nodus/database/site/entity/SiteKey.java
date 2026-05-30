package net.nodus.database.site.entity;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nodus.database.common.MutableDocument;
import net.nodus.database.site.SiteKeyStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("site_keys")
@CompoundIndex(
    name = "site_active_key_unique",
    def = "{'siteId': 1, 'status': 1}",
    unique = true,
    partialFilter = "{'status': 'ACTIVE'}"
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteKey extends MutableDocument {

    @Id
    private String id;

    @Indexed
    private String userAccountId;

    @Indexed
    private String siteId;

    @Indexed
    private String keyPrefix;

    private String keyHash;

    @Indexed
    private SiteKeyStatus status = SiteKeyStatus.ACTIVE;

    private Instant revokedAt;

    public SiteKey(String userAccountId, String siteId, String keyPrefix, String keyHash) {
        this.userAccountId = userAccountId;
        this.siteId = siteId;
        this.keyPrefix = keyPrefix;
        this.keyHash = keyHash;
    }
}
