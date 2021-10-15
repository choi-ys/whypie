package me.whypie.domain.service

import me.whypie.domain.generator.LoginUserGenerator
import me.whypie.domain.generator.MemberGenerator
import me.whypie.domain.model.dto.request.member.SignupRequest
import me.whypie.domain.model.entity.member.Member
import me.whypie.domain.repository.MemberRepo
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
import java.util.*

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

    @Test
    @DisplayName("특정 회원 조회")
    fun findById() {
        // Given
        val memberMock = MemberGenerator.member()
        given(memberRepo.findById(anyLong()))
            .willReturn(Optional.of(memberMock))

        // When
        val expected = memberService.findById(memberMock.id)

        // Then
        verify(memberRepo, times(1)).findById(anyLong())
        assertAll(
            { assertEquals(expected.email, memberMock.email) },
            { assertEquals(expected.name, memberMock.name) },
            { assertEquals(expected.nickname, memberMock.nickname) },
        )
    }

    @Test
    @DisplayName("내 정보 조회")
    fun me() {
        // Given
        val memberMock = MemberGenerator.member()
        given(memberRepo.findByEmail(anyString()))
            .willReturn(Optional.of(memberMock))

        val loginUserMock = LoginUserGenerator.generateLoginUserMock(memberMock)

        // When
        memberService.me(loginUserMock)

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString())
    }
}