package me.whypie.service

import me.whypie.generator.TokenGenerator
import me.whypie.model.dto.request.LoginRequest
import me.whypie.model.entity.Member
import me.whypie.model.vo.Principal
import me.whypie.repository.MemberRepo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-11 오후 7:23
 */
@ExtendWith(MockitoExtension::class)
@DisplayName("Service:Login")
internal class LoginServiceTest {

    @Mock
    lateinit var memberRepo: MemberRepo

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var authorizationService: AuthorizationService

    @InjectMocks
    lateinit var loginService: LoginService

    @Test
    @DisplayName("로그인")
    fun login() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "choi-ys"
        val nickname = "whypie"
        val member = Member(email = email, password = password, name = name, nickname = nickname)

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(member))

        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true)

        given(authorizationService.issue(Principal.mapTo(member.email, member.roles)))
            .willReturn(TokenGenerator.generateTokenMock())

        val loginRequest = LoginRequest(email, password)

        // When
        loginService.login(loginRequest)

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString())
        verify(passwordEncoder, times(1)).matches(anyString(), anyString())
        verify(authorizationService, times(1)).issue(Principal.mapTo(member.email, member.roles))
    }
}