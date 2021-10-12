package me.whypie.model.dto.response

import me.whypie.model.entity.member.Member
import me.whypie.model.vo.Token

/**
 * @author : choi-ys
 * @date : 2021-10-11 오후 7:17
 */
data class LoginResponse(
    val id: Long,
    val email: String,
    val name: String,
    val nickname: String,
    val token: Token,
) {
    companion object {
        fun mapTo(member: Member, token: Token): LoginResponse {
            return LoginResponse(member.id, member.email, member.name, member.nickname, token)
        }
    }
}