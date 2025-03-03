package me.whypie.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.assertions.AssertionException.Companion.assertHttpMessageNotReadable
import me.whypie.assertions.AssertionException.Companion.assertMethodArgumentNotValid
import me.whypie.config.EnableMockMvc
import me.whypie.config.docs.RestDocsConfiguration
import me.whypie.domain.assertions.AssertionProject.Companion.assertDetailResponse
import me.whypie.domain.assertions.AssertionProject.Companion.assertPageResponse
import me.whypie.domain.generator.MemberGenerator
import me.whypie.domain.generator.ProjectGenerator
import me.whypie.domain.model.dto.request.project.CreateProjectRequest
import me.whypie.domain.model.dto.request.project.PatchProjectRequest
import me.whypie.domain.model.dto.request.project.PatchProjectStatusRequest
import me.whypie.domain.model.entity.project.ProjectStatus
import me.whypie.domain.model.entity.project.ProjectType
import me.whypie.generator.TokenGenerator
import me.whypie.utils.generator.docs.ProjectDocumentGenerator.Companion.generateCreateProjectDocument
import me.whypie.utils.generator.docs.ProjectDocumentGenerator.Companion.generateInvalidCreateProjectDocument
import me.whypie.utils.generator.docs.ProjectDocumentGenerator.Companion.generateProjectPaginationDocument
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021-10-13 오후 3:28
 */
@SpringBootTest
@EnableMockMvc
@ActiveProfiles("test")
@DisplayName("Application:API:Project")
@Transactional
@AutoConfigureRestDocs
@Import(MemberGenerator::class, TokenGenerator::class, ProjectGenerator::class, RestDocsConfiguration::class)
internal class ProjectControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Autowired
    lateinit var projectGenerator: ProjectGenerator

    @Autowired
    lateinit var restDocsConfiguration: RestDocsConfiguration

    private val PROJECT_URL = "/project"

    @Test
    @DisplayName("[200:POST]프로젝트 생성")
    fun create() {
        // Given
        val savedMember = memberGenerator.savedCertifiedMember()
        val name = "Cloud:M"
        val domain = "cloudm.co.kr"
        val type = ProjectType.BACK_OFFICE
        val createProjectRequest = CreateProjectRequest(name, domain, type)

        val accessToken = tokenGenerator.accessToken(savedMember.email, savedMember.mapToSimpleGrantedAuthority())

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(PROJECT_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(accessToken))
                .content(objectMapper.writeValueAsString(createProjectRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("name").value(name))
            .andExpect(jsonPath("domain").value(domain))
            .andExpect(jsonPath("type").value(type.name))
            .andExpect(jsonPath("status").value(ProjectStatus.DISABLE.name))
            .andExpect(jsonPath("creator.id").value(savedMember.id))
            .andExpect(jsonPath("creator.email").value(savedMember.email))
            .andExpect(jsonPath("creator.name").value(savedMember.name))
            .andExpect(jsonPath("creator.nickname").value(savedMember.nickname))
            .andDo(generateCreateProjectDocument(restDocsConfiguration.restDocumentationResultHandler()))
    }

    @Test
    @DisplayName("[400:POST]프로젝트 생성 실패:값이 없는 요청")
    fun create_Fail_Cause_EmptyParam() {
        // Given
        val savedMember = memberGenerator.savedCertifiedMember()
        val accessToken = tokenGenerator.accessToken(savedMember.email, savedMember.mapToSimpleGrantedAuthority())

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(PROJECT_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(accessToken))
        )

        // Then
        assertHttpMessageNotReadable(resultActions)
    }

    @Test
    @DisplayName("[400:POST]프로젝트 생성 실패:값이 잘못된 요청")
    fun create_Fail_Cause_InvalidParam() {
        // Given
        val savedMember = memberGenerator.savedCertifiedMember()
        val accessToken = tokenGenerator.accessToken(savedMember.email, savedMember.mapToSimpleGrantedAuthority())
        val createProjectRequest = CreateProjectRequest("", "", enumValueOf("SERVICE"))

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(PROJECT_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(accessToken))
                .content(objectMapper.writeValueAsString(createProjectRequest))
        )

        // Then
        assertMethodArgumentNotValid(resultActions)
        resultActions.andDo(generateInvalidCreateProjectDocument(restDocsConfiguration.restDocumentationResultHandler()))
    }

    @Test
    @DisplayName("[200:GET]특정 사용자의 프로젝트 조회: 첫페이지 조회")
    fun findAllByMemberId_DefaultPageRequest() {
        // Given
        val savedMember = memberGenerator.savedUnCertifiedMember()
        projectGenerator.generateProjectList(savedMember, 5)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("$PROJECT_URL/member")
                .param("memberId", savedMember.id.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
        assertPageResponse(resultActions = resultActions)
    }

    @Test
    @DisplayName("[200:GET]특정 사용자의 프로젝트 조회: 특정 페이지")
    fun findAllByMemberId() {
        // Given
        val savedMember = memberGenerator.savedUnCertifiedMember()
        projectGenerator.generateProjectList(savedMember, 5)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("$PROJECT_URL/member")
                .param("memberId", savedMember.id.toString())
                .param("page", "2")
                .param("size", "3")
                .param("sort", "createdAt")
                .param("direction", Sort.Direction.DESC.name)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
        assertPageResponse(resultActions = resultActions)
    }

    @Test
    @DisplayName("[200:GET]특정 사용자의 프로젝트 조회: 조회 결과 없음")
    fun findAllByMemberId_EmptyProjectList() {
        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("$PROJECT_URL/member")
                .param("memberId", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("totalPages").exists())
            .andExpect(jsonPath("totalElementCount").exists())
            .andExpect(jsonPath("currentPage").exists())
            .andExpect(jsonPath("currentElementCount").exists())
            .andExpect(jsonPath("perPageNumber").exists())
            .andExpect(jsonPath("firstPage").exists())
            .andExpect(jsonPath("lastPage").exists())
            .andExpect(jsonPath("hasNextPage").exists())
            .andExpect(jsonPath("hasPrevious").exists())
            .andExpect(jsonPath("$.embedded[*]").isEmpty)
            .andDo(generateProjectPaginationDocument(restDocsConfiguration.restDocumentationResultHandler()))
    }

    @Test
    @DisplayName("[200:GET]특정 프로젝트 조회")
    fun findById() {
        // Given
        val savedMember = memberGenerator.savedUnCertifiedMember()
        val savedProject = projectGenerator.savedProject(savedMember)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("$PROJECT_URL/${savedProject.id}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(savedProject.name))
            .andExpect(jsonPath("domain").value(savedProject.domain))
            .andExpect(jsonPath("type").value(savedProject.type.name))
            .andExpect(jsonPath("status").value(savedProject.status.name))
        assertDetailResponse(resultActions = resultActions, given = savedProject)
    }

    @Test
    @DisplayName("[200:PATCH]프로젝트 상태 수정")
    fun updateStatus() {
        // Given
        val savedMember = memberGenerator.savedCertifiedMember()
        val savedProject = projectGenerator.savedProject(savedMember)
        val accessToken = tokenGenerator.accessToken(savedMember.email, savedMember.mapToSimpleGrantedAuthority())

        val patchProjectStatusRequest = PatchProjectStatusRequest(ProjectStatus.ENABLE)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("$PROJECT_URL/status/${savedProject.id}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(accessToken))
                .content(objectMapper.writeValueAsString(patchProjectStatusRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(savedProject.name))
            .andExpect(jsonPath("domain").value(savedProject.domain))
            .andExpect(jsonPath("type").value(savedProject.type.name))
            .andExpect(jsonPath("status").value(patchProjectStatusRequest.status.name))
        assertDetailResponse(resultActions = resultActions, given = savedProject)
    }

    @Test
    @DisplayName("[200:PATCH]프로젝트 정보 수정")
    fun update() {
        // Given
        val savedMember = memberGenerator.savedCertifiedMember()
        val savedProject = projectGenerator.savedProject(savedMember)
        val accessToken = tokenGenerator.accessToken(savedMember.email, savedMember.mapToSimpleGrantedAuthority())

        val updateRequestName = "updated name"
        val updateRequestDomain = "update.domain.com"
        val updateRequestType = ProjectType.SERVICE
        val patchProjectRequest = PatchProjectRequest(updateRequestName, updateRequestDomain, updateRequestType)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("$PROJECT_URL/${savedProject.id}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(accessToken))
                .content(objectMapper.writeValueAsString(patchProjectRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(updateRequestName))
            .andExpect(jsonPath("domain").value(updateRequestDomain))
            .andExpect(jsonPath("type").value(updateRequestType.name))
            .andExpect(jsonPath("status").value(savedProject.status.name))
        assertDetailResponse(resultActions = resultActions, given = savedProject)
    }
}