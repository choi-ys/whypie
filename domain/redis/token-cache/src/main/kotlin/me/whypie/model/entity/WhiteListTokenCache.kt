package me.whypie.model.entity

import me.whypie.model.vo.Token
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

/**
 * @author : choi-ys
 * @date : 2021/10/06 5:07 오후
 */
@RedisHash(value = "WHITE_LIST_", timeToLive = 600L)
data class WhiteListTokenCache(
    @Id
    val id: String,
    val token: Token,
)