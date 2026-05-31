package net.nodus.database.sdk;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SiteVisitLogRepository extends MongoRepository<SiteVisitLog, String> {

    @Query("{ 'siteId': ?0, 'createdAt': { $gte: ?1, $lt: ?2 } }")
    List<SiteVisitLog> findAllBySiteIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
        String siteId,
        LocalDateTime start,
        LocalDateTime end
    );
}
