package me.whypie.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.model.dto.request.SignupRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * @author : choi-ys
 * @date : 2021/10/05 11:47 오전
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Application:API:Member")
internal class MemberControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    final val MEMBER_URL = "/member"

    @Test
    @DisplayName("[200:POST]회원가입")
    fun signup() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "choi-ys"
        val nickname = "whypie"

        val signupRequest = SignupRequest(email, password, name, nickname)

        // When
        val resultActions = mockMvc.perform(post(MEMBER_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(signupRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }
}