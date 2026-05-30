package net.nodus.core.analytics;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import net.nodus.core.analytics.dto.CreateAnalyticsEventRequest;
import net.nodus.core.site.SiteKeyService;
import net.nodus.database.analytics.AnalyticsEvent;
import net.nodus.database.analytics.AnalyticsEventRepository;
import net.nodus.database.analytics.ReferrerType;
import net.nodus.database.site.entity.SiteKey;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsEventService {

    private final SiteKeyService siteKeyService;
    private final AnalyticsEventRepository analyticsEventRepository;

    public AnalyticsEventService(SiteKeyService siteKeyService,
        AnalyticsEventRepository analyticsEventRepository) {
        this.siteKeyService = siteKeyService;
        this.analyticsEventRepository = analyticsEventRepository;
    }

    public AnalyticsEvent create(CreateAnalyticsEventRequest request) {
        SiteKey siteKey = siteKeyService.authenticate(request.getSiteKey());

        if (AnalyticsEventTypes.PAGE_VIEW.equals(request.getEventType())) {
            updatePreviousPageDuration(siteKey.getSiteId(), request.getSessionId(),
                request.getTime());
        }

        return analyticsEventRepository.save(new AnalyticsEvent(
            Objects.requireNonNull(siteKey.getId()),
            siteKey.getUserAccountId(),
            siteKey.getSiteId(),
            request.getSessionId(),
            request.getEventType(),
            request.getEventName(),
            request.getBaseUrl(),
            request.getLastPage(),
            request.getCurrentPage(),
            classifyReferrer(request.getBaseUrl(), request.getLastPage()),
            request.getProperties(),
            request.getTime()
        ));
    }

    private void updatePreviousPageDuration(String siteId, String sessionId,
        Instant currentEventTime) {
        analyticsEventRepository
            .findFirstBySiteIdAndSessionIdAndEventTypeAndEventTimeLessThanOrderByEventTimeDesc(
                siteId, sessionId, AnalyticsEventTypes.PAGE_VIEW, currentEventTime)
            .ifPresent(previousEvent -> {
                long durationMs = Duration.between(previousEvent.getEventTime(), currentEventTime)
                    .toMillis();
                if (durationMs > 0) {
                    previousEvent.setDurationMs(durationMs);
                    analyticsEventRepository.save(previousEvent);
                }
            });
    }

    private ReferrerType classifyReferrer(String baseUrl, String lastPage) {
        if (lastPage == null || lastPage.isBlank()) {
            return ReferrerType.DIRECT;
        }

        String baseHost = parseHost(baseUrl);
        if (baseHost == null) {
            return ReferrerType.UNKNOWN;
        }
        String lastHost = parseHost(lastPage);
        if (lastHost == null) {
            return ReferrerType.UNKNOWN;
        }

        return isSameSite(baseHost, lastHost) ? ReferrerType.INTERNAL : ReferrerType.EXTERNAL;
    }

    private String parseHost(String url) {
        try {
            String host = URI.create(url).getHost();
            if (host == null) {
                return null;
            }
            return host.replaceFirst("^www\\.", "").toLowerCase(Locale.ROOT);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private boolean isSameSite(String baseHost, String targetHost) {
        return targetHost.equals(baseHost)
            || targetHost.endsWith("." + baseHost)
            || baseHost.endsWith("." + targetHost);
    }
}
