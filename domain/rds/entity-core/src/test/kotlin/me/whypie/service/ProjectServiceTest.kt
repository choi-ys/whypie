package me.whypie.service

import me.whypie.generator.MemberGenerator
import me.whypie.generator.ProjectGenerator
import me.whypie.model.dto.request.CreateProjectRequest
import me.whypie.model.entity.project.Project
import me.whypie.repository.MemberRepo
import me.whypie.repository.ProjectRepo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/10/12 4:30 오후
 */
@ExtendWith(MockitoExtension::class)
@DisplayName("RDS:Service:Project")
internal class ProjectServiceTest {

    @Mock
    lateinit var memberRepo: MemberRepo

    @Mock
    lateinit var projectRepo: ProjectRepo

    @InjectMocks
    lateinit var projectService: ProjectService

    @Test
    @DisplayName("프로젝트 생성")
    fun create() {
        // Given
        val projectMock = ProjectGenerator.generateProjectMock()
        given(memberRepo.findById(anyLong())).willReturn(Optional.of(MemberGenerator.member()))
        given(projectRepo.existsByName(anyString())).willReturn(false)
        given(projectRepo.save(projectMock)).will(AdditionalAnswers.returnsFirstArg<Project>())

        val createProjectRequest = CreateProjectRequest(0L, projectMock.name, projectMock.domain, projectMock.type)

        // When
        val expected = projectService.create(createProjectRequest)

        // Then
        verify(memberRepo, times(1)).findById(anyLong())
        verify(projectRepo, times(1)).existsByName(anyString())
        verify(projectRepo, times(1)).save(projectMock)

        assertAll(
            { assertEquals(expected.name, projectMock.name) },
            { assertEquals(expected.domain, projectMock.domain) },
            { assertEquals(expected.type, projectMock.type) },
            { assertEquals(expected.status, projectMock.status) }
        )
    }

    @Test
    @DisplayName("프로젝트 생성 실패: 중복된 Name")
    fun create_Fail_Cause_DuplicatedName() {
        // Given
        val projectMock = ProjectGenerator.generateProjectMock()
        given(memberRepo.findById(anyLong())).willReturn(Optional.of(MemberGenerator.member()))
        given(projectRepo.existsByName(anyString())).willReturn(true)

        val createProjectRequest = CreateProjectRequest(0L, projectMock.name, projectMock.domain, projectMock.type)

        // When
        assertThrows(IllegalArgumentException::class.java) {
            projectService.create(createProjectRequest)
        }.let {
            assertTrue(it is RuntimeException)
        }

        // Then
        verify(projectRepo, times(1)).existsByName(anyString())
    }
}