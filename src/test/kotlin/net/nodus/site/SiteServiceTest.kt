package net.nodus.site

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.nodus.config.exception.GlobalException
import net.nodus.project.Project
import net.nodus.project.ProjectRepository
import net.nodus.site.dto.CreateSiteRequest
import net.nodus.site.entity.Site
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class SiteServiceTest {

    private val siteRepository = mockk<SiteRepository>()
    private val siteKeyRepository = mockk<SiteKeyRepository>()
    private val siteKeyService = mockk<SiteKeyService>()
    private val projectRepository = mockk<ProjectRepository>()

    private val siteService = SiteService(
        siteRepository = siteRepository,
        siteKeyRepository = siteKeyRepository,
        siteKeyService = siteKeyService,
        projectRepository = projectRepository,
    )

    @Test
    fun `create creates site under project`() {
        val userId = "user-1"
        val project = Project(
            id = "project-1",
            userAccountId = userId,
            workspaceId = "workspace-1",
            name = "Project",
        )

        val savedSite = Site(
            id = "site-1",
            userAccountId = userId,
            workspaceId = "workspace-1",
            projectId = "project-1",
            name = "Site",
            domain = "example.com",
            url = "https://example.com"
        )

        every {
            projectRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("project-1", userId)
        } returns project

        every { siteRepository.save(any()) } returns savedSite

        every { siteKeyService.issue(savedSite) } returns mockk()

        val result = siteService.create(
            userId = userId,
            request = CreateSiteRequest(
                projectId = "project-1",
                name = "Site",
                domain = "example.com",
                url = "https://example.com"
            ),
        )

        assertEquals("site-1", result.site.id)
        assertEquals("workspace-1", result.site.workspaceId)
        assertEquals("project-1", result.site.projectId)

        verify {
            projectRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("project-1", userId)
            siteRepository.save(any())
            siteKeyService.issue(savedSite)
        }
    }

    @Test
    fun `create throws when project not found`() {
        every {
            projectRepository.findByIdAndUserAccountIdAndDeletedAtIsNull("missing-project", "user-1")
        } returns null

        assertFailsWith<GlobalException.DataNotFound> {
            siteService.create(
                userId = "user-1",
                request = CreateSiteRequest(
                    projectId = "missing-project",
                    name = "Site",
                )
            )
        }
    }
}