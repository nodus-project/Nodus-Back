package net.nodus.site

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.nodus.site.dto.CreateSiteRequest
import net.nodus.site.entity.Site
import kotlin.test.Test
import kotlin.test.assertEquals


class SiteServiceTest {

    private val siteRepository = mockk<SiteRepository>()
    private val siteKeyRepository = mockk<SiteKeyRepository>()
    private val siteKeyService = mockk<SiteKeyService>()

    private val siteService = SiteService(
        siteRepository = siteRepository,
        siteKeyRepository = siteKeyRepository,
        siteKeyService = siteKeyService,
    )

    @Test
    fun `create creates site without workspace`() {
        val userId = "user-1"
        val savedSite = Site(
            id = "site-1",
            userAccountId = userId,
            name = "Site",
            domain = "example.com",
            url = "https://example.com"
        )

        every { siteRepository.save(any()) } returns savedSite

        every { siteKeyService.issue(savedSite) } returns mockk()

        val result = siteService.create(
            userId = userId,
            request = CreateSiteRequest(
                name = "Site",
                domain = "example.com",
                url = "https://example.com"
            ),
        )

        assertEquals("site-1", result.site.id)
        assertEquals("Site", result.site.name)

        verify {
            siteRepository.save(any())
            siteKeyService.issue(savedSite)
        }
    }

    @Test
    fun `list returns user sites`() {
        val sites = listOf(
            Site(
                id = "site-1",
                userAccountId = "user-1",
                name = "Site",
            )
        )

        every {
            siteRepository.findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(
                userAccountId = "user-1",
            )
        } returns sites

        val result = siteService.list(
            userId = "user-1",
        )

        assertEquals(1, result.size)
        assertEquals("site-1", result[0].id)

        verify {
            siteRepository.findByUserAccountIdAndDeletedAtIsNullOrderByCreatedAtDesc(
                userAccountId = "user-1",
            )
        }
    }
}
