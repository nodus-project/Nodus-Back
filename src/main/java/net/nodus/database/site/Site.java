package net.nodus.database.site;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nodus.database.account.UserAccount;
import net.nodus.database.common.BaseEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sites")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site extends BaseEntity {

    @Id
    private String id;

    private String name;
    private String url;

    public void update(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Builder.Default
    private String key = null;

    public void recreateKey() {
        this.key = this.id + UUID.randomUUID() + LocalDateTime.now();
    }

    @Builder.Default
    private List<SiteAllowedUser> allowedUserList = new ArrayList<>();

    public void addAllowedUser(UserAccount userAccount) {
        SiteAllowedUser siteAllowedUser = SiteAllowedUser.builder()
            .id(userAccount.getId())
            .build();
        allowedUserList.add(siteAllowedUser);
    }

    @Builder
    public record SiteAllowedUser(
        String id
    ) {

    }
}
