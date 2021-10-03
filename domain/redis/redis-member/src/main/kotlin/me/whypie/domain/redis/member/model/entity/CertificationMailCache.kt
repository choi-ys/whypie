package me.whypie.domain.redis.member.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

/**
 * @author : choi-ys
 * @date : 2021-10-03 오후 9:53
 */
@RedisHash(value = "CERTIFICATION_", timeToLive = 600L)
data class CertificationMailCache(
    @Id
    var email: String,
    var certificationNumber: Int = (100000..200000).random()
)