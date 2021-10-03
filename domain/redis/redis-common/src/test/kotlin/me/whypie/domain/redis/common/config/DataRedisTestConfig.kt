package me.whypie.domain.redis.common.config

import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

/**
 * @author : choi-ys
 * @date : 2021-10-03 오후 9:56
 */
@DataRedisTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(EmbeddedRedisConfig::class)
@ActiveProfiles("test")
open class DataRedisTestConfig {
}