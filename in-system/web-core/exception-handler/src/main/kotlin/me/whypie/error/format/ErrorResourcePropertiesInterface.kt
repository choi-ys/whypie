package me.whypie.error.format

import java.time.LocalDateTime

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:48 오후
 */
interface ErrorResourcePropertiesInterface {
    var timestamp: LocalDateTime

    val code: String
    val message: String

    val method: String
    val path: String
}