package net.nodus.database.analytics;

import java.time.Instant;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nodus.database.common.BaseDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("analytics_events")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalyticsEvent extends BaseDocument {

    @Id
    private String id;

    @Indexed
    private String siteKeyId;

    @Indexed
    private String userAccountId;

    @Indexed
    private String siteId;

    @Indexed
    private String sessionId;

    @Indexed
    private String eventType;

    @Indexed
    private String eventName;

    private String baseUrl;
    private String lastPage;
    private String currentPage;
    private ReferrerType referrerType;
    private Long durationMs;
    private Map<String, Object> properties = Map.of();

    @Indexed
    private Instant eventTime;

    public AnalyticsEvent(
        String siteKeyId,
        String userAccountId,
        String siteId,
        String sessionId,
        String eventType,
        String eventName,
        String baseUrl,
        String lastPage,
        String currentPage,
        ReferrerType referrerType,
        Map<String, Object> properties,
        Instant eventTime
    ) {
        this.siteKeyId = siteKeyId;
        this.userAccountId = userAccountId;
        this.siteId = siteId;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.eventName = eventName;
        this.baseUrl = baseUrl;
        this.lastPage = lastPage;
        this.currentPage = currentPage;
        this.referrerType = referrerType;
        this.properties = properties == null ? Map.of() : properties;
        this.eventTime = eventTime;
    }
}
