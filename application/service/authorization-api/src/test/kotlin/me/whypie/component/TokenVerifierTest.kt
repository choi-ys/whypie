package me.whypie.component

import me.whypie.generator.TokenGenerator
import me.whypie.utils.LocalDateTimeUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author : choi-ys
 * @date : 2021-10-09 오전 4:08
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@DisplayName("Component:TokenProvider")
@Import(TokenGenerator::class)
internal class TokenVerifierTest(
    private val tokenGenerator: TokenGenerator,
    private val tokenVerifier: TokenVerifier
) {

    @Test
    @DisplayName("Token 검증")
    fun verify() {
        // Given
        val user = TokenGenerator.generateUserMock();
        val tokenMock = tokenGenerator.generateTokenMock(user)

        // When
        val accessTokenVerifyResult = tokenVerifier.verify(tokenMock.accessToken)
        val refreshTokenVerifyResult = tokenVerifier.verify(tokenMock.refreshToken)

        // Then
        val accessExpiredLocalDateTime = LocalDateTimeUtils.timestampToLocalDateTime(tokenMock.accessExpired.time)
        val refreshExpiredLocalDateTime = LocalDateTimeUtils.timestampToLocalDateTime(tokenMock.refreshExpired.time)
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