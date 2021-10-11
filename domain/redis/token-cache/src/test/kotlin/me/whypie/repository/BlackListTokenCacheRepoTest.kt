package me.whypie.repository

import me.whypie.config.DataRedisTestConfig
import me.whypie.model.entity.BlackListTokenCache
import me.whypie.model.vo.Token
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.NoSuchElementException

/**
 * @author : choi-ys
 * @date : 2021/10/06 5:13 오후
 */
@DisplayName("Redis:Repo:BlackListTokenCacheRepo")
internal class BlackListTokenCacheRepoTest(
    private val blackListTokenCacheRepo: BlackListTokenCacheRepo,
) : DataRedisTestConfig() {

    private fun savedWhiteListCache(): BlackListTokenCache {
        val id = "id"
        val token = Token("access-token", "refresh-token", Date(), Date())
        val blackListTokenCache = BlackListTokenCache(id, token)
        return blackListTokenCacheRepo.save(blackListTokenCache)
    }

    @Test
    @DisplayName("발급 토큰 캐싱")
    internal fun save() {
        // Given
        val id = "id"
        val token = Token("access-token", "refresh-token", Date(), Date())
        val blackListTokenCache = BlackListTokenCache(id, token)

        // When
        val expected = blackListTokenCacheRepo.save(blackListTokenCache)

        // Then
        assertAll(
            { assertEquals(expected.id, id) },
            { assertTrue(expected.token == token) },
        )
    }

    @Test
    @DisplayName("[Hit]토큰 캐시 조회")
    internal fun findById() {
        // Given
        val savedWhiteListCache = savedWhiteListCache()

        // When
        val expected = blackListTokenCacheRepo.findById(savedWhiteListCache.id).orElseThrow()

        // Then
        assertAll(
            { assertEquals(expected.id, savedWhiteListCache.id) },
            { assertEquals(expected.token, savedWhiteListCache.token) }
        )
    }

    @Test
    @DisplayName("[Fail]토큰 캐시 조회")
    internal fun findById_Fail_CauseInvalidId() {
        // Given
        val invalidId = "invalidId"

        // When
        assertThrows(NoSuchElementException::class.java) {
            blackListTokenCacheRepo.findById(invalidId).orElseThrow()
        }.let {
            assertTrue(it is RuntimeException)
        }
    }
}