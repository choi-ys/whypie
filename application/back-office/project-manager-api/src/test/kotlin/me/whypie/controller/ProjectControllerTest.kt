package me.whypie.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.config.EnableMockMvc
import me.whypie.generator.MemberGenerator
import me.whypie.generator.ProjectGenerator
import me.whypie.generator.TokenGenerator
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
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
@Disabled
@Import(MemberGenerator::class, TokenGenerator::class)
internal class ProjectControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Test
    @DisplayName("[200:POST]회원가입")
    fun create() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val project = ProjectGenerator.generateProject(savedMember)
        val accessToken = tokenGenerator.accessToken(savedMember.email, savedMember.roles)

        val urlTemplate = "/project"

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(accessToken))
                .content(objectMapper.writeValueAsString(project))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("domain").value(project.domain))
            .andExpect(jsonPath("type").value(project.type.name))
            .andExpect(jsonPath("status").value(project.status.name))
    }
}