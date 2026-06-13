package net.nodus.database.sdk;

import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteVisitLogRepository extends JpaRepository<SiteVisitLog, UUID> {

    @Query("""
        select count(distinct siteVisitLog.sessionId)
        from SiteVisitLog siteVisitLog
        where siteVisitLog.site = :site
          and siteVisitLog.sessionId <> ''
        """)
    Long countDistinctSessionId(
        @Param("site")
        Site site
    );

    @Query("""
        select count(distinct siteVisitLog.sessionId)
        from SiteVisitLog siteVisitLog
        where siteVisitLog.site = :site
          and siteVisitLog.sessionId <> ''
          and exists (
              select 1
              from SiteVisitLog retentionLog
              where retentionLog.site = :site
                and retentionLog.sessionId = siteVisitLog.sessionId
                and function('date', retentionLog.createdAt)
                    <> function('date', siteVisitLog.createdAt)
          )
        """)
    Long countRetentionSessionId(
        @Param("site")
        Site site
    );
}
