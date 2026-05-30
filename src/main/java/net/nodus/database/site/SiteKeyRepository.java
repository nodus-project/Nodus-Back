package net.nodus.database.site;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import net.nodus.database.site.entity.SiteKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SiteKeyRepository extends MongoRepository<SiteKey, String> {

    Optional<SiteKey> findBySiteIdAndStatusAndDeletedAtIsNull(String siteId, SiteKeyStatus status);

    List<SiteKey> findAllByKeyPrefixAndStatusAndDeletedAtIsNull(String keyPrefix,
        SiteKeyStatus status);

    List<SiteKey> findByDeletedAtBefore(Instant deletedAt);
}
