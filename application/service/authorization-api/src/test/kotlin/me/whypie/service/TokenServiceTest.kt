package me.whypie.service

import me.whypie.component.TokenProvider
import me.whypie.component.TokenVerifier
import me.whypie.generator.TokenGenerator
import me.whypie.model.entity.BlackListTokenCache
import me.whypie.model.entity.WhiteListTokenCache
import me.whypie.repository.BlackListTokenCacheRepo
import me.whypie.repository.WhiteListTokenCacheRepo
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
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-09 오전 4:07
 */
@ExtendWith(MockitoExtension::class)
@DisplayName("Service:Token")
internal class TokenServiceTest {

    @Mock
    lateinit var tokenProvider: TokenProvider

    @Mock
    lateinit var tokenVerifier: TokenVerifier

    @Mock
    lateinit var blackListTokenCacheRepo: BlackListTokenCacheRepo

    @Mock
    lateinit var whiteListTokenCacheRepo: WhiteListTokenCacheRepo

    @InjectMocks
    lateinit var tokenService: TokenService

    @Test
    @DisplayName("토큰 발급")
    fun issue() {
        // Given
        val userMock = TokenGenerator.generateUserMock()
        val tokenMock = TokenGenerator.generateTokenMock()
        val whiteListTokenCache = WhiteListTokenCache(userMock.username, tokenMock)

        given(tokenProvider.createToken(userMock))
            .willReturn(tokenMock)

        given(whiteListTokenCacheRepo.save(whiteListTokenCache))
            .will(AdditionalAnswers.returnsFirstArg<WhiteListTokenCache>())

        // When
        val expected = tokenService.issue(userMock)

        // Then
        verify(tokenProvider, times(1)).createToken(userMock)
        verify(whiteListTokenCacheRepo, times(1)).save(whiteListTokenCache)

        assertAll(
            { assertEquals(expected.accessToken, tokenMock.accessToken) },
            { assertEquals(expected.refreshToken, tokenMock.refreshToken) },
            { assertEquals(expected.accessExpired, tokenMock.accessExpired) },
            { assertEquals(expected.refreshExpired, tokenMock.refreshExpired) }
        )
    }

    @Test
    @DisplayName("토큰 갱신")
    fun refresh() {
        // Given
        val userMock = TokenGenerator.generateUserMock()
        val issuedTokenMock = TokenGenerator.generateTokenMock()
        val createdTokenMock = TokenGenerator.generateTokenMock()

        given(tokenVerifier.verify(issuedTokenMock.refreshToken))
            .willReturn(TokenGenerator.getVerifyResult(issuedTokenMock.refreshToken))

        given(tokenProvider.createToken(userMock)).willReturn(createdTokenMock)

        given(whiteListTokenCacheRepo.findById(userMock.username))
            .willReturn(Optional.of(WhiteListTokenCache(userMock.username, createdTokenMock)))

        // When
        tokenService.refresh(issuedTokenMock.refreshToken)

        // Then
        verify(tokenVerifier, times(1)).verify(issuedTokenMock.refreshToken)
        verify(tokenProvider, times(1)).createToken(userMock)
        verify(whiteListTokenCacheRepo, times(1)).findById(userMock.username)
    }

    @Test
    @DisplayName("토큰 만료")
    fun expire() {
        // Given
        val userMock = TokenGenerator.generateUserMock()
        val issuedTokenMock = TokenGenerator.generateTokenMock()

        given(tokenVerifier.verify(issuedTokenMock.accessToken))
            .willReturn(TokenGenerator.getVerifyResult(issuedTokenMock.accessToken))

        given(whiteListTokenCacheRepo.findById(userMock.username))
            .willReturn(Optional.of(WhiteListTokenCache(userMock.username, issuedTokenMock)))

        // When
        tokenService.expire(issuedTokenMock.accessToken)

        // Then
        verify(tokenVerifier, times(1)).verify(issuedTokenMock.accessToken)
        verify(whiteListTokenCacheRepo, times(1)).findById(anyString())
        verify(blackListTokenCacheRepo, times(1)).save(any(BlackListTokenCache::class.java))
    }
}