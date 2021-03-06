package me.whypie.model.vo

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/10/06 5:08 오후
 */
data class Token(
    val accessToken: String,

    val refreshToken: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    val accessExpired: Date,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    val refreshExpired: Date,
)