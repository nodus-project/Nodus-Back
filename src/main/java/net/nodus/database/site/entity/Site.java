package net.nodus.database.site.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nodus.database.common.MutableDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sites")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site extends MutableDocument {

    @Id
    private String id;

    @Indexed
    private String userAccountId;

    private String name;
    private String domain;
    private String url;

    public Site(String userAccountId, String name, String domain, String url) {
        this(null, userAccountId, name, domain, url);
    }

    public Site(String id, String userAccountId, String name, String domain, String url) {
        this.id = id;
        this.userAccountId = userAccountId;
        this.name = name;
        this.domain = domain;
        this.url = url;
    }
}
