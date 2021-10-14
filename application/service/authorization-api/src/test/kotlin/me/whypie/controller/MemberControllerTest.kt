package me.whypie.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.whypie.config.EnableMockMvc
import me.whypie.error.ErrorCode
import me.whypie.domain.model.dto.request.SignupRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021-10-11 오전 5:33
 */
@SpringBootTest
@EnableMockMvc
@ActiveProfiles("test")
@DisplayName("Application:API:Member")
@Transactional
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
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequest))
        )

        // Then
        resultActions.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("email").value(signupRequest.email))
            .andExpect(MockMvcResultMatchers.jsonPath("name").value(signupRequest.name))
            .andExpect(MockMvcResultMatchers.jsonPath("nickname").value(signupRequest.nickname))
    }

    @Test
    @DisplayName("[400:POST]회원 가입 실패(값이 없는 요청)")
    fun signup_Fail_CauseNoArgument() {
        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
        )

        // Then
        resultActions.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("method").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("path").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.name))
            .andExpect(MockMvcResultMatchers.jsonPath("message").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.message))
    }

    @Test
    @DisplayName("[400:POST]회원 가입 실패(값이 잘못된 요청)")
    fun signup_Fail_CauseInvalidArgument() {
        // Given
        val signupRequest = SignupRequest("", "", "", "")

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(signupRequest))
        )

        // Then
        resultActions.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("method").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("path").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.name))
            .andExpect(MockMvcResultMatchers.jsonPath("message").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.message))
            .andExpect(MockMvcResultMatchers.jsonPath("errorDetails").isNotEmpty)
    }
}