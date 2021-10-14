package me.whypie.domain.model.dto.request

import me.whypie.domain.model.entity.member.Member
import me.whypie.domain.model.entity.project.Project
import me.whypie.domain.model.entity.project.ProjectType

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:19 오후
 */
data class CreateProjectRequest(
    val name: String,
    val domain: String,
    val type: ProjectType,
) {
    fun toEntity(member: Member): Project {
        return Project(name = name, domain = domain, type = type, member = member)
    }
}