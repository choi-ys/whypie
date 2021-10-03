package me.whypie.domain.redis.member.repository

import me.whypie.domain.redis.common.config.DataRedisTestConfig
import me.whypie.domain.redis.member.model.entity.CertificationMailCache
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * @author : choi-ys
 * @date : 2021-10-03 오후 10:07
 */
@DisplayName("Redis:Repository:CertificationMailCache")
internal class CertificationMailCacheRepoTest(
    private val certificationMailCacheRepo: CertificationMailCacheRepo
) : DataRedisTestConfig() {

    private fun savedCertificationMailCache(): CertificationMailCache {
        val email = "project.log.062@gmail.com"
        return certificationMailCacheRepo.save(CertificationMailCache(email = email))
    }

    @Test
    @DisplayName("인증 메일 캐시 저장")
    internal fun name() {
        // Given
        val email = "project.log.062@gmail.com"
        val certificationMailCache = CertificationMailCache(email = email)

        // When
        val savedCertificationMailCache = certificationMailCacheRepo.save(certificationMailCache)

        // Then
        val expected: CertificationMailCache = certificationMailCacheRepo.findById(email).orElseThrow()

        assertEquals(expected.email, email)
        assertEquals(savedCertificationMailCache.email, email)
    }

    @Test
    @DisplayName("[Hit]만료 토큰 조회")
    internal fun findById() {
        // Given
        val savedCertificationMailCache = savedCertificationMailCache()

        // When
        val expected = certificationMailCacheRepo.findById(savedCertificationMailCache.email).orElseThrow()

        // When
        assertAll(
            { assertEquals(expected.email, savedCertificationMailCache.email) },
            { assertEquals(expected.certificationNumber, savedCertificationMailCache.certificationNumber) }
        )
    }

    @Test
    @DisplayName("[Fail]만료 토큰 조회")
    internal fun findById_HitFail() {
        // Given
        val invalidId = "project.log.062@gmail.com"

        // When & Then
        assertThrows(RuntimeException::class.java) {
            certificationMailCacheRepo.findById(invalidId).orElseThrow()
        }.let {
            assertEquals(it.javaClass.simpleName, NoSuchElementException::class.java.simpleName)
        }
    }
}