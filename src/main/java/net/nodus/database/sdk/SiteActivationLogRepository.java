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
    List<SiteActivationLog> findLogList(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );

    @Query("""
        select count(distinct siteActivationLog.sessionId)
        from SiteActivationLog siteActivationLog
        where siteActivationLog.site = :site
          and siteActivationLog.createdAt >= :start
          and siteActivationLog.createdAt < :end
          and siteActivationLog.sessionId <> ''
        """)
    Long countDistinctSessionId(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );

    @Query("""
        select count(distinct siteActivationLog.sessionId)
        from SiteActivationLog siteActivationLog
        where siteActivationLog.site = :site
          and siteActivationLog.createdAt >= :start
          and siteActivationLog.createdAt < :end
          and siteActivationLog.sessionId <> ''
          and not exists (
              select 1
              from SiteActivationLog previousLog
              where previousLog.site = :site
                and previousLog.sessionId = siteActivationLog.sessionId
                and previousLog.createdAt < :start
          )
        """)
    Long countFirstEventUsers(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );

    @Query("""
        select siteActivationLog.featureName as name,
               count(siteActivationLog.id) as count
        from SiteActivationLog siteActivationLog
        where siteActivationLog.site = :site
          and siteActivationLog.createdAt >= :start
          and siteActivationLog.createdAt < :end
          and siteActivationLog.featureName is not null
          and siteActivationLog.featureName <> ''
        group by siteActivationLog.featureName
        order by count(siteActivationLog.id) desc
        """)
    List<ActivationNameCount> countByFeatureName(
        @Param("site")
        Site site,
        @Param("start")
        LocalDateTime start,
        @Param("end")
        LocalDateTime end
    );

    interface FirstEventUser {

        String getSessionId();

        LocalDateTime getFirstEventAt();
    }

    interface ActivationNameCount {

        String getName();

        Long getCount();
    }
}
