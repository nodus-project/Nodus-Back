package net.nodus.database.auth;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nodus.database.common.MutableDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends MutableDocument {

    @Id
    private String id;

    @Indexed
    private String userAccountId;

    @Indexed(unique = true)
    private String tokenHash;

    private Instant expiresAt;
    private Instant revokedAt;

    public RefreshToken(String userAccountId, String tokenHash, Instant expiresAt) {
        this.userAccountId = userAccountId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }
}
