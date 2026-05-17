package net.nodus.site

import net.nodus.common.exception.GlobalException
import net.nodus.site.dto.IssuedSiteKey
import net.nodus.site.entity.Site
import net.nodus.site.entity.SiteKey
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64

@Service
class SiteKeyService(
    private val siteKeyRepository: SiteKeyRepository,
    private val siteRepository: SiteRepository,
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun issue(site: Site): IssuedSiteKey {
        val siteId = requireNotNull(site.id)

        revokeActive(siteId)

        val rawKey = generateRawKey()
        val siteKey = siteKeyRepository.save(
            SiteKey(
                userAccountId = site.userAccountId,
                siteId = siteId,
                keyPrefix = rawKey.take(KEY_PREFIX_LENGTH),
                keyHash = requireNotNull(passwordEncoder.encode(rawKey)),
            )
        )

        return IssuedSiteKey(
            siteKey = siteKey,
            rawKey = rawKey,
        )
    }

    fun authenticate(rawKey: String): SiteKey {
        val candidates = siteKeyRepository.findAllByKeyPrefixAndStatusAndDeletedAtIsNull(
            keyPrefix = rawKey.take(KEY_PREFIX_LENGTH),
            status = SiteKeyStatus.ACTIVE,
        )

        val siteKey = candidates.firstOrNull {
            passwordEncoder.matches(rawKey, it.keyHash)
        } ?: throw GlobalException.Unauthorized("Invalid key")

        siteRepository.findByIdAndUserAccountIdAndDeletedAtIsNull(
            id = siteKey.siteId,
            userAccountId = siteKey.userAccountId,
        ) ?: throw GlobalException.DataNotFound("Site not found")

        return siteKey
    }

    fun revokeActive(siteId: String) {
        val activeKey = siteKeyRepository.findBySiteIdAndStatusAndDeletedAtIsNull(
            siteId = siteId,
            status = SiteKeyStatus.ACTIVE,
        ) ?: return

        activeKey.status = SiteKeyStatus.REVOKED
        activeKey.revokedAt = Instant.now()

        siteKeyRepository.save(activeKey)
    }

    private fun generateRawKey(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)

        return "nodus_live_" + Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }

    companion object {
        private const val KEY_PREFIX_LENGTH = 24
    }
}
