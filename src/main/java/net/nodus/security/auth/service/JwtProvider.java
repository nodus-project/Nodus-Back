package net.nodus.security.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.crypto.SecretKey;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserRole;
import net.nodus.security.auth.controller.dto.UserAccountDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration-seconds}")
    private Long expirationSeconds;

    public String createAccessToken(UserAccountDetails user) {
        String userId = Objects.requireNonNull(user.getUsername());
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
            .subject(userId)
            .claim("name", user.getName())
            .claim("role", user.getUserRole())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(signingKey(), Jwts.SIG.HS256)
            .compact();
    }

    public String createRefreshToken(UserAccountDetails user) {
        String userId = Objects.requireNonNull(user.getUsername());
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
            .subject(userId)
            .claim("name", user.getName())
            .claim("role", user.getUserRole())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(signingKey(), Jwts.SIG.HS256)
            .compact();
    }

    public UserAccountDetails parseAccessToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(signingKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        UserAccount user = UserAccount.builder()
            .id(UUID.fromString(claims.getSubject()))
            .name(claims.get("name", String.class))
            .userRole(UserRole.valueOf(claims.get("role", String.class)))
            .build();

        return new UserAccountDetails(user);
    }

    public UserAccountDetails parseRefreshToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(signingKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        UserAccount user = UserAccount.builder()
            .id(UUID.fromString(claims.getSubject()))
            .name(claims.get("name", String.class))
            .userRole(UserRole.valueOf(claims.get("role", String.class)))
            .build();

        return new UserAccountDetails(user);
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
