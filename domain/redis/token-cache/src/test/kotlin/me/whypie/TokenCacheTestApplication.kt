package me.whypie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author : choi-ys
 * @date : 2021/10/06 5:13 오후
 */
@SpringBootApplication
class TokenCacheTestApplication

fun main(args: Array<String>) {
    runApplication<TokenCacheTestApplication>(*args)
}