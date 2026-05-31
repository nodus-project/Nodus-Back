package net.nodus.database.site;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SiteRepository extends MongoRepository<Site, String> {

    List<Site> findAllByAllowedUserListIdAndDeletedAtIsNull(String userId);

    Optional<Site> findByIdAndAllowedUserListIdAndDeletedAtIsNull(
        String siteId,
        String userId
    );
}
