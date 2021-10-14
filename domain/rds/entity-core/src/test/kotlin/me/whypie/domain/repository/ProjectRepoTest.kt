package me.whypie.domain.repository

import me.whypie.config.DataJpaTestConfig
import me.whypie.domain.assertions.AssertionProject.Companion.assertEntity
import me.whypie.domain.generator.MemberGenerator
import me.whypie.domain.generator.ProjectGenerator
import me.whypie.domain.model.dto.request.PatchProjectRequest
import me.whypie.domain.model.entity.project.Project
import me.whypie.domain.model.entity.project.ProjectStatus
import me.whypie.domain.model.entity.project.ProjectType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.TestConstructor
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:31 오후
 */
@DisplayName("RDS:Repo:Project")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(MemberGenerator::class, ProjectGenerator::class)
class ProjectRepoTest(
    private val projectRepo: ProjectRepo,
    private val memberGenerator: MemberGenerator,
    private val projectGenerator: ProjectGenerator,
) : DataJpaTestConfig() {

    @Test
    @DisplayName("프로젝트 객체 저장")
    fun save() {
        // Given
        val name = "whypie"
        val domain = "whypie.me"
        val type = ProjectType.BACK_OFFICE
        val savedMember = memberGenerator.savedUnCertifiedMember()
        val project = Project(name = name, domain = domain, type = type, member = savedMember)

        // When
        val expected = projectRepo.save(project)

        // Then
        assertEntity(expected = expected, given = project)
    }

    @Test
    @DisplayName("프로젝트 객체 저장 실패: 중복된 name")
    fun save_Fail_Cause_duplicatedName() {
        // Given
        val savedMember = memberGenerator.savedUnCertifiedMember()
        val project = ProjectGenerator.generateProject(savedMember)
        val duplicatedProject = ProjectGenerator.generateProject(savedMember)

        // When & Then
        projectRepo.save(project)
        assertThrows(Exception::class.java) { projectRepo.save(duplicatedProject) }
    }

    @Test
    @DisplayName("프로젝트 객체 조회: Id")
    fun findById() {
        // Given
        val savedMember = memberGenerator.savedUnCertifiedMember()
        val savedProject = projectRepo.save(projectGenerator.savedProject(savedMember))
        entityManger.flush()
        entityManger.clear()

        // When
        val expected = projectRepo.findById(savedProject.id).orElseThrow()

        // Then
        assertEntity(expected = expected, given = savedProject)
    }

    @Test
    @DisplayName("프로젝트 객체 조회 실패: 존재하지 않는 Id")
    fun findById_Fail_Cause_NotExistId() {
        // Given
        val invalidId = 0L

        // When & Then
        assertThrows(NoSuchElementException::class.java) {
            projectRepo.findById(invalidId).orElseThrow()
        }.let {
            assertTrue(it is RuntimeException)
        }
    }

    @Test
    @DisplayName("특정 사용자의 프로젝트 목록 조회")
    fun findAllByMemberId() {
        // Given
        val savedMember = memberGenerator.savedUnCertifiedMember()
        val count = 10
        projectGenerator.generateProjectList(savedMember, count)

        val requestPage = 1
        val pageSize = 10
        val pageRequest = PageRequest.of(requestPage - 1, pageSize)

        // When
        val expected = projectRepo.findAllByMemberId(savedMember.id, pageRequest)

        // Then
        assertAll(
            { assertEquals(expected.totalPages, requestPage, "조회된 전체 페이지 수") },
            { assertEquals(expected.totalElements, count.toLong(), "조회된 전체 컨텐츠 개수") },
            { assertEquals(expected.number, requestPage - 1, "현재 페이지 번호") },
            { assertEquals(expected.numberOfElements, count, "현재 페이지의 컨텐츠 개수") },
            { assertEquals(expected.size, pageSize, "조회 페이지당 요소 수") },
            {
                expected.stream()
                    .map { it.member.id }
                    .collect(Collectors.toSet()).let {
                        assertEquals(it.size, 1)
                        assertTrue(it.contains(savedMember.id))
                    }
            }
        )
    }

    @Test
    @DisplayName("프로젝트 상태 변경")
    fun updateProjectStatus() {
        // Given
        val savedCertifiedMember = memberGenerator.savedCertifiedMember()
        val savedProject = projectGenerator.savedProject(savedCertifiedMember)
        flushAndClear()

        // When
        val expected = projectRepo.findByIdAndMemberEmail(savedProject.id, savedCertifiedMember.email).orElseThrow()
        expected.updateStatus(ProjectStatus.ENABLE)
        flush()

        // Then
        assertEquals(expected.status, ProjectStatus.ENABLE)
    }

    @Test
    @DisplayName("프로젝트 정보 수정")
    fun updateProject() {
        // Given
        val savedCertifiedMember = memberGenerator.savedCertifiedMember()
        val savedProject = projectGenerator.savedProject(savedCertifiedMember)
        flushAndClear()

        val updateRequestName = "updated name";
        val updateRequestDomain = "update.domain.com"
        val updateRequestType = ProjectType.SERVICE
        val patchProjectRequest = PatchProjectRequest(updateRequestName, updateRequestDomain, updateRequestType)

        // When
        val expected = projectRepo.findByIdAndMemberEmail(savedProject.id, savedCertifiedMember.email).orElseThrow()
        expected.update(patchProjectRequest)
        flush()

        // Then
        assertAll(
            { assertEquals(expected.name, updateRequestName) },
            { assertEquals(expected.domain, updateRequestDomain) },
            { assertEquals(expected.type, updateRequestType) }
        )
    }
}