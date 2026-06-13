package net.nodus.database.sdk;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nodus.database.common.BaseEntity;
import net.nodus.database.site.Site;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteTrafficSource extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    public void settingSite(Site site) {
        this.site = site;
    }

    @Column(nullable = false, unique = true)
    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Comment("유입 채널")
    private TrafficChannel channel;

    @Comment("유입 출처")
    private String source;

    @Column(length = 100)
    @Comment("유입 방식")
    private String medium;

    @Comment("캠페인 이름")
    private String campaign;

    @Comment("검색어 또는 광고 키워드")
    private String term;

    @Comment("광고 소재 또는 링크 위치 구분값")
    private String content;

    @Column(columnDefinition = "TEXT")
    @Comment("HTTP Referer 전체 URL")
    private String referrer;

    @Comment("HTTP Referer 호스트")
    private String referrerHost;

    @Column(columnDefinition = "TEXT")
    @Comment("최초 유입 페이지 전체 URL")
    private String landingPage;

    @Column(length = 500)
    @Comment("최초 유입 페이지 경로")
    private String landingPath;

    @Column(length = 500)
    @Comment("광고 클릭 ID")
    private String clickId;
}
