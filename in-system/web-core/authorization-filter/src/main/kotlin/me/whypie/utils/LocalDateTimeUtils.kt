package me.whypie.utils

import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-08 오후 9:57
 */
class LocalDateTimeUtils {

    companion object {
        fun timestampToLocalDateTime(timestamp: Long): LocalDateTime {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId()
            )
        }
    }
}