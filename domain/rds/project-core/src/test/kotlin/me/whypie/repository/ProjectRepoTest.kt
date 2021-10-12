package me.whypie.repository

import me.whypie.config.DataJpaTestConfig
import me.whypie.model.entity.Project
import me.whypie.model.entity.ProjectStatus
import me.whypie.model.entity.ProjectType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.test.context.TestConstructor

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:31 오후
 */
@DisplayName("Rds:Repo:Project")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ProjectRepoTest(
    private val projectRepo: ProjectRepo,
) : DataJpaTestConfig() {

    private fun generateProject(): Project {
        val name = "whypie"
        val type = ProjectType.BACK_OFFICE
        val domain = "whypie.me"
        return Project(name = name, type = type, domain = domain)
    }

    @Test
    @DisplayName("프로젝트 객체 저장")
    fun save() {
        // Given
        val name = "whypie"
        val type = ProjectType.BACK_OFFICE
        val domain = "whypie.me"
        val project = Project(name = name, type = type, domain = domain)

        // When
        val expected = projectRepo.save(project)

        // Then
        assertAll(
            { assertNotEquals(expected.id, 0L) },
            { assertEquals(expected.name, name) },
            { assertEquals(expected.type, type) },
            { assertEquals(expected.domain, domain) },
            { assertEquals(expected.status, ProjectStatus.DISABLE) },
        )
    }

    @Test
    @DisplayName("프로젝트 객체 저장 실패: 중복된 name")
    fun save_Fail_Cause_duplicatedName() {
        // Given
        val project = generateProject()
        val duplicatedProject = generateProject()

        // When & Then
        projectRepo.save(project)
        assertThrows(Exception::class.java) { projectRepo.save(duplicatedProject) }
    }

    @Test
    @DisplayName("프로젝트 객체 조회: Id")
    fun findById() {
        // Given
        val savedProject = projectRepo.save(generateProject())
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
        assertThrows(NoSuchElementException::class.java){
            projectRepo.findById(invalidId).orElseThrow()
        }.let {
            assertTrue(it is RuntimeException)
        }
    }
}