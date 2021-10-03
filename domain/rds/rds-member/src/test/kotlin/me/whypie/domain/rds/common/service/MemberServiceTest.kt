package me.whypie.domain.rds.common.service

import me.whypie.domain.rds.common.model.dto.request.SignupRequest
import me.whypie.domain.rds.common.model.entity.Member
import me.whypie.domain.rds.common.repository.MemberRepo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:55
 */
@DisplayName("RDS:Service:Member")
@ExtendWith(MockitoExtension::class)
internal class MemberServiceTest {

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var memberRepo: MemberRepo

    @InjectMocks
    lateinit var memberService: MemberService

    @Test
    @DisplayName("회원 가입")
    fun signup() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "name"
        val nickname = "nickname"
        val signupRequest = SignupRequest(email = email, password = password, name = name, nickname = nickname)

        given(memberRepo.existsByEmail(signupRequest.email)).willReturn(false)
        given(passwordEncoder.encode(signupRequest.password)).willReturn("encoded password")
        given(memberRepo.save(any(Member::class.java))).will(AdditionalAnswers.returnsFirstArg<Member>())

        // When
        val expected = memberService.signup(signupRequest)

        // Then
        verify(memberRepo, times(1)).existsByEmail(signupRequest.email)
        verify(passwordEncoder, times(1)).encode(password)
        verify(memberRepo, times(1)).save(signupRequest.toEntity(passwordEncoder))
        assertAll(
            { assertEquals(expected.email, email) },
            { assertEquals(expected.name, name) },
            { assertEquals(expected.nickname, nickname) },
        )
    }
}