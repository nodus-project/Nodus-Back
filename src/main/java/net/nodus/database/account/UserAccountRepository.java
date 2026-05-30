package net.nodus.database.account;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    Optional<UserAccount> findByIdAndDeletedAtIsNull(String id);

    Optional<UserAccount> findByProviderAndProviderIdAndDeletedAtIsNull(OAuthProvider provider,
        String providerId);
}
