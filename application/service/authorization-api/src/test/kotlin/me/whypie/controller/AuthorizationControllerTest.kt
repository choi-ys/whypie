package me.whypie.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.config.EnableMockMvc
import me.whypie.generator.TokenGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021-10-10 오후 7:31
 */
@SpringBootTest
@EnableMockMvc
@ActiveProfiles("test")
@DisplayName("Application:API:Authorization")
@Transactional
@Import(TokenGenerator::class)
internal class AuthorizationControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Test
    @DisplayName("[200:POST]토큰 발급")
    fun issue() {
        // Given
        val urlTemplate = "/auth/token"
        val principalMock = TokenGenerator.generatePrincipalMock()

        // When
        val resultActions = mockMvc.perform(
            post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(principalMock))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("accessToken").exists())
            .andExpect(jsonPath("refreshToken").exists())
            .andExpect(jsonPath("accessExpired").exists())
            .andExpect(jsonPath("refreshExpired").exists())
    }

    // TODO TokenExpiredException, JWTDecodeException, SignatureVerificationException, 탈취된 Refresh token
    @Test
    @DisplayName("[200:POST]토큰 갱신")
    fun refresh() {
        // Given
        val urlTemplate = "/auth/refresh"
        val tokenMock = tokenGenerator.issuedToken()

        // When
        val resultActions = mockMvc.perform(
            post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + tokenMock.refreshToken)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("accessToken").exists())
            .andExpect(jsonPath("refreshToken").exists())
            .andExpect(jsonPath("accessExpired").exists())
            .andExpect(jsonPath("refreshExpired").exists())
    }

    @Test
    @DisplayName("[206:POST]토큰 만료")
    fun expire() {
        // Given
        val urlTemplate = "/auth/expire"
        val tokenMock = tokenGenerator.issuedToken()

        // When
        val resultActions = mockMvc.perform(
            delete(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + tokenMock.accessToken)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isNoContent)
    }
}