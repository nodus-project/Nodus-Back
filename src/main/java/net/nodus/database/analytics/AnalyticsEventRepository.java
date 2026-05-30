package net.nodus.database.analytics;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnalyticsEventRepository extends MongoRepository<AnalyticsEvent, String> {

    Optional<AnalyticsEvent> findFirstBySiteIdAndSessionIdAndEventTypeAndEventTimeLessThanOrderByEventTimeDesc(
        String siteId,
        String sessionId,
        String eventType,
        Instant eventTime
    );
}
