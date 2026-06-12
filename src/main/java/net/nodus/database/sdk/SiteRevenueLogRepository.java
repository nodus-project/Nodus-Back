package net.nodus.database.sdk;

import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteRevenueLogRepository extends JpaRepository<SiteRevenueLog, UUID> {

    @Query("""
        select count(distinct siteRevenueLog.sessionId)
        from SiteRevenueLog siteRevenueLog
        where siteRevenueLog.site = :site
          and siteRevenueLog.sessionId <> ''
        """)
    Long countDistinctSessionId(
        @Param("site")
        Site site
    );

}
