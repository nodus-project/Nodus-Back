package net.nodus.core.site;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import net.nodus.global.common.exception.GlobalException;
import net.nodus.core.site.dto.IssuedSiteKey;
import net.nodus.database.site.SiteKeyRepository;
import net.nodus.database.site.SiteKeyStatus;
import net.nodus.database.site.SiteRepository;
import net.nodus.database.site.entity.Site;
import net.nodus.database.site.entity.SiteKey;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SiteKeyService {

    private static final int KEY_PREFIX_LENGTH = 24;

    private final SiteKeyRepository siteKeyRepository;
    private final SiteRepository siteRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom secureRandom = new SecureRandom();

    public SiteKeyService(SiteKeyRepository siteKeyRepository, SiteRepository siteRepository) {
        this.siteKeyRepository = siteKeyRepository;
        this.siteRepository = siteRepository;
    }

    public IssuedSiteKey issue(Site site) {
        String siteId = Objects.requireNonNull(site.getId());

        revokeActive(siteId);

        String rawKey = generateRawKey();
        SiteKey siteKey = siteKeyRepository.save(new SiteKey(
            site.getUserAccountId(),
            siteId,
            rawKey.substring(0, KEY_PREFIX_LENGTH),
            Objects.requireNonNull(passwordEncoder.encode(rawKey))
        ));

        return new IssuedSiteKey(siteKey, rawKey);
    }

    public SiteKey authenticate(String rawKey) {
        var candidates = siteKeyRepository.findAllByKeyPrefixAndStatusAndDeletedAtIsNull(
            rawKey.substring(0, Math.min(KEY_PREFIX_LENGTH, rawKey.length())),
            SiteKeyStatus.ACTIVE
        );

        SiteKey siteKey = candidates.stream()
            .filter(candidate -> passwordEncoder.matches(rawKey, candidate.getKeyHash()))
            .findFirst()
            .orElseThrow(() -> new GlobalException.Unauthorized("Invalid key"));

        siteRepository
            .findByIdAndUserAccountIdAndDeletedAtIsNull(siteKey.getSiteId(),
                siteKey.getUserAccountId())
            .orElseThrow(() -> new GlobalException.DataNotFound("Site not found"));

        return siteKey;
    }

    public void revokeActive(String siteId) {
        siteKeyRepository.findBySiteIdAndStatusAndDeletedAtIsNull(siteId, SiteKeyStatus.ACTIVE)
            .ifPresent(activeKey -> {
                activeKey.setStatus(SiteKeyStatus.REVOKED);
                activeKey.setRevokedAt(Instant.now());
                siteKeyRepository.save(activeKey);
            });
    }

    private String generateRawKey() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return "nodus_live_" + Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes);
    }
}
