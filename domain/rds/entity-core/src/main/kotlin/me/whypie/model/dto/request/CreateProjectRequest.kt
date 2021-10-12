package me.whypie.model.dto.request

import me.whypie.model.entity.member.Member
import me.whypie.model.entity.project.Project
import me.whypie.model.entity.project.ProjectType

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:19 오후
 */
data class CreateProjectRequest(
    val memberId: Long,
    val name: String,
    val domain: String,
    val type: ProjectType,
) {
    fun toEntity(member: Member): Project {
        return Project(name = name, domain = domain, type = type, member = member)
    }
}