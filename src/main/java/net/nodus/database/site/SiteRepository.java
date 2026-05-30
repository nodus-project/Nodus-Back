package net.nodus.database.site;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import net.nodus.database.site.entity.Site;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SiteRepository extends MongoRepository<Site, String> {

    List<Site> findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(String userAccountId);

    Optional<Site> findByIdAndUserAccountIdAndDeletedAtIsNull(String id, String userAccountId);

    List<Site> findByDeletedAtBefore(Instant deletedAt);
}
