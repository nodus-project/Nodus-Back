package net.nodus.database.account;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nodus.database.common.BaseEntity;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount extends BaseEntity {

    @Id
    private String id;

    private String email;

    private String name;

    private OAuthProvider provider;

    private String providerId;

    @Builder.Default
    private UserRole userRole = UserRole.USER;

}
