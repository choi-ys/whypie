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
internal class AuthorizationServiceTest {

    @Mock
    lateinit var tokenProvider: TokenProvider

    @Mock
    lateinit var tokenVerifier: TokenVerifier

    @Mock
    lateinit var blackListTokenCacheRepo: BlackListTokenCacheRepo

    @Mock
    lateinit var whiteListTokenCacheRepo: WhiteListTokenCacheRepo

    @InjectMocks
    lateinit var authorizationService: AuthorizationService

    @Test
    @DisplayName("토큰 발급")
    fun issue() {
        // Given
        val principalMock = TokenGenerator.generatePrincipalMock()
        val tokenMock = TokenGenerator.generateTokenMock()
        val whiteListTokenCache = WhiteListTokenCache(principalMock.identifier, tokenMock)

        given(tokenProvider.createToken(principalMock))
            .willReturn(tokenMock)

        given(whiteListTokenCacheRepo.save(whiteListTokenCache))
            .will(AdditionalAnswers.returnsFirstArg<WhiteListTokenCache>())

        // When
        val expected = authorizationService.issue(principalMock)

        // Then
        verify(tokenProvider, times(1)).createToken(principalMock)
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
        val principalMock = TokenGenerator.generatePrincipalMock()
        val issuedTokenMock = TokenGenerator.generateTokenMock()
        val createdTokenMock = TokenGenerator.generateTokenMock()

        given(tokenVerifier.verify(issuedTokenMock.refreshToken))
            .willReturn(TokenGenerator.getVerifyResult(issuedTokenMock.refreshToken))

        given(tokenProvider.createToken(principalMock)).willReturn(createdTokenMock)

        given(whiteListTokenCacheRepo.findById(principalMock.identifier))
            .willReturn(Optional.of(WhiteListTokenCache(principalMock.identifier, createdTokenMock)))

        // When
        authorizationService.refresh(issuedTokenMock.refreshToken)

        // Then
        verify(tokenVerifier, times(1)).verify(issuedTokenMock.refreshToken)
        verify(tokenProvider, times(1)).createToken(principalMock)
        verify(whiteListTokenCacheRepo, times(1)).findById(principalMock.identifier)
    }

    @Test
    @DisplayName("토큰 만료")
    fun expire() {
        // Given
        val principalMock = TokenGenerator.generatePrincipalMock()
        val issuedTokenMock = TokenGenerator.generateTokenMock()

        given(tokenVerifier.verify(issuedTokenMock.accessToken))
            .willReturn(TokenGenerator.getVerifyResult(issuedTokenMock.accessToken))

        given(whiteListTokenCacheRepo.findById(principalMock.identifier))
            .willReturn(Optional.of(WhiteListTokenCache(principalMock.identifier, issuedTokenMock)))

        // When
        authorizationService.expire(issuedTokenMock.accessToken)

        // Then
        verify(tokenVerifier, times(1)).verify(issuedTokenMock.accessToken)
        verify(whiteListTokenCacheRepo, times(1)).findById(anyString())
        verify(blackListTokenCacheRepo, times(1)).save(any(BlackListTokenCache::class.java))
    }
}