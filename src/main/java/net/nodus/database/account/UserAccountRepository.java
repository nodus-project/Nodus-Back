package net.nodus.database.account;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    Optional<UserAccount> findByProviderAndProviderIdAndDeletedAtIsNull(OAuthProvider provider,
        String providerId);

    List<UserAccount> findByDeletedAtBefore(Instant deletedAt);
}
