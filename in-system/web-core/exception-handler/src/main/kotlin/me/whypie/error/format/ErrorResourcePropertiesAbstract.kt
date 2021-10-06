package me.whypie.error.format

import java.time.LocalDateTime

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:48 오후
 */
abstract class ErrorResourcePropertiesAbstract(
    open val timestamp: LocalDateTime = LocalDateTime.now(),
    open val code: String,
    open val message: String,
    open val method: String,
    open val path: String
)