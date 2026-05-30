package net.nodus.database.auth;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(String tokenHash);

    List<RefreshToken> findByDeletedAtBefore(Instant deletedAt);
}
