package net.nodus.database.site;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nodus.database.common.BaseDeleteEntity;
import net.nodus.database.sdk.SiteActivationLog;
import net.nodus.database.sdk.SiteRevenueLog;
import net.nodus.database.sdk.SiteTrafficSource;
import net.nodus.database.sdk.SiteVisitLog;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

@Entity
@SQLDelete(sql = "UPDATE sites SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site extends BaseDeleteEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    public void updateBaseInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Builder.Default
    @Column(unique = true)
    private String key = null;

    public void recreateKey() {
        this.key = this.id.toString() + UUID.randomUUID() + LocalDateTime.now();
    }

    @Builder.Default
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SiteAllowedUser> allowedUserList = new ArrayList<>();

    public void addAllowedUser(SiteAllowedUser allowedUser) {
        allowedUser.settingSite(this);
        allowedUserList.add(allowedUser);
    }

    @Builder.Default
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SiteTrafficSource> trafficSourceList = new ArrayList<>();

    public void addTrafficSource(SiteTrafficSource trafficSource) {
        trafficSource.settingSite(this);
        trafficSourceList.add(trafficSource);
    }

    @Builder.Default
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SiteVisitLog> visitLogList = new ArrayList<>();

    public void addVisitLog(SiteVisitLog visitLog) {
        visitLog.settingSite(this);
        visitLogList.add(visitLog);
    }

    @Builder.Default
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SiteActivationLog> activationLogList = new ArrayList<>();

    public void addActivationLog(SiteActivationLog activationLog) {
        activationLog.settingSite(this);
        activationLogList.add(activationLog);
    }

    @Builder.Default
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SiteRevenueLog> revenueLogList = new ArrayList<>();

    public void addRevenueLog(SiteRevenueLog revenueLog) {
        revenueLog.settingSite(this);
        revenueLogList.add(revenueLog);
    }

}
