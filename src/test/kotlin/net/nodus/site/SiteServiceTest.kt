package net.nodus.site

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.nodus.config.exception.GlobalException
import net.nodus.site.dto.CreateSiteRequest
import net.nodus.site.entity.Site
import net.nodus.workspace.Workspace
import net.nodus.workspace.WorkspaceRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class SiteServiceTest {

    private val siteRepository = mockk<SiteRepository>()
    private val siteKeyRepository = mockk<SiteKeyRepository>()
    private val siteKeyService = mockk<SiteKeyService>()
    private val workspaceRepository = mockk<WorkspaceRepository>()

    private val siteService = SiteService(
        siteRepository = siteRepository,
        siteKeyRepository = siteKeyRepository,
        siteKeyService = siteKeyService,
        workspaceRepository = workspaceRepository
    )

    @Test
    fun `create creates site under workspace`() {
        val userId = "user-1"
        val workspace = Workspace(
            id = "workspace-1",
            userAccountId = userId,
            name = "Workspace"
        )

        val savedSite = Site(
            id = "site-1",
            userAccountId = userId,
            workspaceId = "workspace-1",
            name = "Site",
            domain = "example.com",
            url = "https://example.com"
        )

        every {
            workspaceRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("workspace-1", userId)
        } returns workspace

        every { siteRepository.save(any()) } returns savedSite

        every { siteKeyService.issue(savedSite) } returns mockk()

        val result = siteService.create(
            userId = userId,
            request = CreateSiteRequest(
                workspaceId = "workspace-1",
                name = "Site",
                domain = "example.com",
                url = "https://example.com"
            ),
        )

        assertEquals("site-1", result.site.id)
        assertEquals("workspace-1", result.site.workspaceId)
        assertEquals("Site", result.site.name)

        verify {
            workspaceRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("workspace-1", userId)
            siteRepository.save(any())
            siteKeyService.issue(savedSite)
        }
    }

    @Test
    fun `create throws when workspace not found`() {
        every {
            workspaceRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("workspace-1", "user-1")
        } returns null

        assertFailsWith<GlobalException.DataNotFound> {
            siteService.create(
                userId = "user-1",
                request = CreateSiteRequest(
                    workspaceId = "workspace-1",
                    name = "Site",
                ),
            )
        }

        verify {
            workspaceRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("workspace-1", "user-1")
        }
    }

    @Test
    fun `list returns sites in workspace`() {
        val sites = listOf(
            Site(
                id = "site-1",
                userAccountId = "user-1",
                workspaceId = "workspace-1",
                name = "Site",
            )
        )

        every {
            siteRepository.findByUserAccountIdAndWorkspaceIdAndDeletedAtIsNullOrderByCreatedAtDesc(
                userAccountId = "user-1",
                workspaceId = "workspace-1"
            )
        } returns sites

        val result = siteService.list(
            userId = "user-1",
            workspaceId = "workspace-1",
        )

        assertEquals(1, result.size)
        assertEquals("site-1", result[0].id)

        verify {
            siteRepository.findByUserAccountIdAndWorkspaceIdAndDeletedAtIsNullOrderByCreatedAtDesc(
                userAccountId = "user-1",
                workspaceId = "workspace-1"
            )
        }
    }
}