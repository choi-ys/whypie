package me.whypie.repository

import me.whypie.config.DataJpaTestConfig
import me.whypie.generator.MemberGenerator
import me.whypie.generator.ProjectGenerator
import me.whypie.model.entity.project.Project
import me.whypie.model.entity.project.ProjectStatus
import me.whypie.model.entity.project.ProjectType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.context.annotation.Import
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
        val savedMember = memberGenerator.savedMember()
        val project = Project(name = name, domain = domain, type = type, member = savedMember)

        // When
        val expected = projectRepo.save(project)

        // Then
        assertAll(
            { assertNotEquals(expected.id, 0L) },
            { assertEquals(expected.name, name) },
            { assertEquals(expected.type, type) },
            { assertEquals(expected.domain, domain) },
            { assertEquals(expected.status, ProjectStatus.DISABLE) },
            { assertEquals(expected.member, savedMember) }
        )
    }

    @Test
    @DisplayName("프로젝트 객체 저장 실패: 중복된 name")
    fun save_Fail_Cause_duplicatedName() {
        // Given
        val savedMember = memberGenerator.savedMember()
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
        val savedMember = memberGenerator.savedMember()
        val savedProject = projectRepo.save(projectGenerator.savedProject(savedMember))
        entityManger.flush()
        entityManger.clear()

        // When
        val expected = projectRepo.findById(savedProject.id).orElseThrow()

        // Then
        assertAll(
            { assertEquals(expected.id, savedProject.id) },
            { assertEquals(expected.name, savedProject.name) },
            { assertEquals(expected.domain, savedProject.domain) },
            { assertEquals(expected.type, savedProject.type) },
            { assertEquals(expected.status, savedProject.status) },
        )
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
        val savedMember = memberGenerator.savedMember()
        val count = 10
        projectGenerator.generateProjectList(savedMember, count)

        // When
        val expected = projectRepo.findAllByMemberId(savedMember.id)

        // Then
        assertAll(
            { assertEquals(expected.size, count) },
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
}