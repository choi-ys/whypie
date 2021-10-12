package me.whypie.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.config.EnableMockMvc
import me.whypie.generator.TokenGenerator
import me.whypie.model.dto.request.LoginRequest
import me.whypie.model.dto.request.SignupRequest
import me.whypie.service.LoginService
import me.whypie.service.MemberService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
 * @date : 2021-10-11 오후 11:51
 */
@SpringBootTest
@EnableMockMvc
@ActiveProfiles("test")
@DisplayName("Application:API:Login")
@Transactional
internal class LoginControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var loginService: LoginService

    @Test
    @DisplayName("[200:POST]로그인")
    fun signup() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "choi-ys"
        val nickname = "whypie"

        val signupRequest = SignupRequest(email, password, name, nickname)
        memberService.signup(signupRequest)

        val urlTemplate = "/login"
        val loginRequest = LoginRequest(email, password)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("name").value(name))
            .andExpect(jsonPath("nickname").value(nickname))
            .andExpect(jsonPath("token.accessToken").exists())
            .andExpect(jsonPath("token.refreshToken").exists())
            .andExpect(jsonPath("token.accessExpired").exists())
            .andExpect(jsonPath("token.refreshExpired").exists())
    }

    @Test
    @DisplayName("[206:POST]로그아웃")
    fun logout() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "choi-ys"
        val nickname = "whypie"

        val signupRequest = SignupRequest(email, password, name, nickname)
        memberService.signup(signupRequest)

        val loginRequest = LoginRequest(email, password)
        val loginResponse = loginService.login(loginRequest)

        val urlTemplate = "/logout"

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(loginResponse.token.accessToken))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isNoContent)
    }
}