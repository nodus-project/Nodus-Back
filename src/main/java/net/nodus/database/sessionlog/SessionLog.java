package net.nodus.database.sessionlog;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nodus.database.common.BaseDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "session_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionLog extends BaseDocument {

    @Id
    private String id;

    @Indexed
    private String siteKeyId;

    @Indexed
    private String siteId;

    @Indexed
    private String userAccountId;

    private String userSession;

    public SessionLog(String siteKeyId, String siteId, String userAccountId, String userSession) {
        this.siteKeyId = siteKeyId;
        this.siteId = siteId;
        this.userAccountId = userAccountId;
        this.userSession = userSession;
    }
}
