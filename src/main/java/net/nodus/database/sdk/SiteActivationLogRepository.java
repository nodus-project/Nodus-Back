package net.nodus.database.sdk;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import net.nodus.database.site.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SiteActivationLogRepository extends JpaRepository<SiteActivationLog, UUID> {

    @Query("""
        select siteActivationLog
        from SiteActivationLog siteActivationLog
        where siteActivationLog.site = :site
          and siteActivationLog.createdAt >= :start
          and siteActivationLog.createdAt < :end
        """)
    List<SiteActivationLog> findActivationLogs(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );
}
