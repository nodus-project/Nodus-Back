package net.nodus.database.sdk;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteVisitLogRepository extends JpaRepository<SiteVisitLog, UUID> {

    @Query("""
        select siteVisitLog
        from SiteVisitLog siteVisitLog
        where siteVisitLog.site = :site
          and siteVisitLog.createdAt >= :start
          and siteVisitLog.createdAt < :end
        """)
    List<SiteVisitLog> findVisitLogs(
        @Param("siteId")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );
}
