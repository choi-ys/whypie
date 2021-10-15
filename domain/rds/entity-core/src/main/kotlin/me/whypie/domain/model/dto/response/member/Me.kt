package me.whypie.domain.model.dto.response.member

import com.fasterxml.jackson.annotation.JsonFormat
import me.whypie.domain.model.entity.member.Member
import java.time.LocalDateTime


/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:43
 */
data class Me(
    val id: Long,
    val email: String,
    val name: String,
    val nickname: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun mapTo(member: Member): Me {
            return Me(
                id = member.id,
                email = member.email,
                name = member.name,
                nickname = member.nickname,
                createdAt = member.createdAt,
                updatedAt = member.updatedAt
            )
        }
    }
}