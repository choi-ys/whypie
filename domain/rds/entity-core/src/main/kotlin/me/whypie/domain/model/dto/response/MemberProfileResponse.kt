package me.whypie.domain.model.dto.response

import me.whypie.domain.model.entity.member.Member


/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:43
 */
data class MemberProfileResponse(
    val id: Long,
    val email: String,
    val name: String,
    val nickname: String,
) {
    companion object {
        fun mapTo(member: Member): MemberProfileResponse {
            return MemberProfileResponse(
                id = member.id,
                email = member.email,
                name = member.name,
                nickname = member.nickname
            )
        }
    }
}