package net.nodus.database.account;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.nodus.database.common.MutableDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_accounts")
@CompoundIndex(
    name = "provider_provider_id_unique",
    def = "{'provider': 1, 'providerId': 1}",
    unique = true
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount extends MutableDocument {

    @Id
    private String id;
    private String email;
    private String name;
    private OAuthProvider provider;
    private String providerId;

    public UserAccount(String email, String name, OAuthProvider provider, String providerId) {
        this(null, email, name, provider, providerId);
    }

    public UserAccount(String id, String email, String name, OAuthProvider provider,
        String providerId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
    }
}
