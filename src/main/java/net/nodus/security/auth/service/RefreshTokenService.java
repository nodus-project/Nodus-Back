package net.nodus.security.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Objects;
import net.nodus.global.common.exception.GlobalException;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.database.auth.RefreshToken;
import net.nodus.database.auth.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userAccountRepository;
    private final long refreshTokenExpirationSeconds;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
        RefreshTokenRepository refreshTokenRepository,
        UserAccountRepository userAccountRepository,
        @Value("${jwt.refresh-token-expiration-seconds}") long refreshTokenExpirationSeconds
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userAccountRepository = userAccountRepository;
        this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
    }

    public IssuedRefreshToken issue(UserAccount user) {
        String token = generateToken();
        String tokenHash = hash(token);

        RefreshToken refreshToken = refreshTokenRepository.save(new RefreshToken(
            Objects.requireNonNull(user.getId()),
            tokenHash,
            Instant.now().plusSeconds(refreshTokenExpirationSeconds)
        ));

        return new IssuedRefreshToken(token, user, refreshToken);
    }

    public IssuedRefreshToken rotate(String rawToken) {
        Instant now = Instant.now();
        RefreshToken refreshToken = refreshTokenRepository
            .findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(hash(rawToken))
            .orElseThrow(() -> new GlobalException.Unauthorized("Invalid refresh token"));

        if (refreshToken.getExpiresAt().isBefore(now)) {
            refreshToken.setRevokedAt(now);
            refreshTokenRepository.save(refreshToken);
            throw new GlobalException.DataExpired("Expired refresh token");
        }

        UserAccount user = userAccountRepository.findById(refreshToken.getUserAccountId())
            .orElseThrow(() -> new GlobalException.Unauthorized("Refresh token user not found"));

        refreshToken.setRevokedAt(now);
        refreshTokenRepository.save(refreshToken);

        return issue(user);
    }

    private String generateToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", ex);
        }
    }

    public record IssuedRefreshToken(String token, UserAccount userAccount,
                                     RefreshToken refreshToken) {

    }
}
