package net.nodus.database.sdk;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteRevenueLogRepository extends JpaRepository<SiteRevenueLog, UUID> {

    @Query("""
        select siteRevenueLog
        from SiteRevenueLog siteRevenueLog
        where siteRevenueLog.site = :site
          and siteRevenueLog.createdAt >= :start
          and siteRevenueLog.createdAt < :end
        """)
    List<SiteRevenueLog> findLogList(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );

    @Query("""
        select count(distinct siteRevenueLog.sessionId)
        from SiteRevenueLog siteRevenueLog
        where siteRevenueLog.site = :site
          and siteRevenueLog.createdAt >= :start
          and siteRevenueLog.createdAt < :end
          and siteRevenueLog.sessionId <> ''
        """)
    Long countDistinctSessionId(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );

}
