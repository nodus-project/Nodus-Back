package net.nodus.database.sdk;

import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteActivationLogRepository extends JpaRepository<SiteActivationLog, UUID> {

    @Query("""
        select count(distinct siteActivationLog.sessionId)
        from SiteActivationLog siteActivationLog
        where siteActivationLog.site = :site
          and siteActivationLog.sessionId <> ''
        """)
    Long countDistinctSessionId(
        @Param("site")
        Site site
    );

}
