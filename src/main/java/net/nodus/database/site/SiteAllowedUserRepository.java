package net.nodus.database.site;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteAllowedUserRepository extends JpaRepository<SiteAllowedUser, UUID> {

    List<SiteAllowedUser> findByUserAccountId(UUID userAccountId);

    Optional<SiteAllowedUser> findBySiteIdAndUserAccountId(UUID siteId, UUID userAccountId);

    UUID site(Site site);
}
