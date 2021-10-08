package me.whypie.component

import me.whypie.model.entity.MemberRole
import me.whypie.utils.LocalDateTimeUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021-10-08 오후 9:37
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@DisplayName("Component:TokenProvider")
internal class TokenProviderTest(
    private val tokenProvider: TokenProvider,
    private val tokenVerifier: TokenVerifier
) {

    @Test
    @DisplayName("Token 생성")
    fun createToken() {
        // Given
        val username = "whypie"
        val roles = setOf(MemberRole.CERTIFIED_MEMBER)
        val user =
            User(
                username,
                "",
                roles.stream().map { it -> SimpleGrantedAuthority("ROlE_" + it.name) }.collect(Collectors.toSet())
            )

        // When
        val createdToken = tokenProvider.createToken(user)

        // Then
        val accessTokenVerifyResult = tokenVerifier.verify(createdToken.accessToken)
        val refreshTokenVerifyResult = tokenVerifier.verify(createdToken.refreshToken)

        val accessExpiredLocalDateTime = LocalDateTimeUtils.timestampToLocalDateTime(createdToken.accessExpired.time)
        val refreshExpiredLocalDateTime = LocalDateTimeUtils.timestampToLocalDateTime(createdToken.refreshExpired.time)
        val ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        assertAll(
            { assertEquals(accessTokenVerifyResult.username, user.username) },
            { assertEquals(refreshTokenVerifyResult.username, accessTokenVerifyResult.username) },
            { assertTrue(accessTokenVerifyResult.authorities!!.containsAll(user.authorities)) },
            { assertTrue(refreshTokenVerifyResult.authorities!!.containsAll(user.authorities)) },
            {
                Assertions.assertEquals(
                    LocalDateTime.now().plusMinutes(10).format(ofPattern),
                    accessExpiredLocalDateTime.format(ofPattern),
                    "발급된 접근 토큰의 유효기간이 10분인지 확인"
                )
            },
            {
                Assertions.assertEquals(
                    LocalDateTime.now().plusMinutes(20).format(ofPattern),
                    refreshExpiredLocalDateTime.format(ofPattern),
                    "발급된 갱신 토큰의 유효기간이 20분인지 확인"
                )
            }
        )
    }

}