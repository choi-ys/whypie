package me.whypie.service

import me.whypie.generator.LoginUserGenerator
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
import org.springframework.data.domain.PageRequest
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
        val memberMock = MemberGenerator.member()
        val loginUserMock = LoginUserGenerator.generateLoginUserMock(memberMock)
        val projectMock = ProjectGenerator.generateProject(memberMock)

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(memberMock))
        given(projectRepo.existsByName(anyString())).willReturn(false)
        given(projectRepo.save(projectMock)).will(AdditionalAnswers.returnsFirstArg<Project>())

        val createProjectRequest = CreateProjectRequest(projectMock.name, projectMock.domain, projectMock.type)

        // When
        val expected = projectService.create(createProjectRequest, loginUserMock)

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString())
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
        val memberMock = MemberGenerator.member()
        val loginUserMock = LoginUserGenerator.generateLoginUserMock(memberMock)
        val projectMock = ProjectGenerator.generateProject(memberMock)

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(MemberGenerator.member()))
        given(projectRepo.existsByName(anyString())).willReturn(true)

        val createProjectRequest = CreateProjectRequest(projectMock.name, projectMock.domain, projectMock.type)

        // When
        assertThrows(IllegalArgumentException::class.java) {
            projectService.create(createProjectRequest, loginUserMock)
        }.let {
            assertTrue(it is RuntimeException)
        }

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString())
        verify(projectRepo, times(1)).existsByName(anyString())
    }

    @Test
    @DisplayName("특정 사용자의 프로젝트 목록 조회")
    fun findAllByMemberId() {
        // Given
        val memberId = 1L
        val requestPage = 1
        val perPageNumber = 10
        val pageRequest = PageRequest.of(requestPage - 1, perPageNumber)
        val generateMockCount = 8
        given(projectRepo.findAllByMemberId(memberId, pageRequest))
            .willReturn(ProjectGenerator.generateProjectPageMock(generateMockCount))

        // When
        val expected = projectService.findAllByMemberId(memberId, pageRequest)

        // Then
        verify(projectRepo, times(1)).findAllByMemberId(memberId, pageRequest)

        println(expected)
        assertAll(
            { assertEquals(expected.totalPages, (generateMockCount / perPageNumber) + 1, "조회된 전체 페이지 수") },
            { assertEquals(expected.totalElementCount, generateMockCount.toLong(), "조회된 전체 컨텐츠 개수") },
            { assertEquals(expected.currentPage, (generateMockCount / perPageNumber) + 1, "현재 페이지 번호") },
            { assertEquals(expected.currentElementCount, generateMockCount, "현제 페이지의 컨텐츠 개수") },
            {
                assertEquals(expected.perPageNumber,
                    if (generateMockCount / perPageNumber > 1) perPageNumber else generateMockCount,
                    "조회 페이지당 요소 개수")
            },
            { assertEquals(expected.firstPage, expected.currentPage == 1, "첫 페이지 여부") },
            { assertEquals(expected.lastPage, expected.totalElementCount / perPageNumber < 1, "마지막 페이지 여부") },
            {
                assertEquals(expected.hasNextPage,
                    expected.totalElementCount / (perPageNumber * requestPage) > 1,
                    "다음 페이지 존재 여부")
            },
            { assertEquals(expected.hasPrevious, requestPage != 1, "이전 페이지 존재 여부") },
            { assertEquals(expected.embedded.size, generateMockCount) }
        )
    }
}