package net.nodus.database.sdk;

import java.util.List;
import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteTrafficSourceRepository extends JpaRepository<SiteTrafficSource, UUID> {

    List<SiteTrafficSource> findAllBySite(Site site);
}
