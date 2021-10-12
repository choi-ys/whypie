package me.whypie.model.dto.response

import me.whypie.model.entity.Project
import me.whypie.model.entity.ProjectStatus
import me.whypie.model.entity.ProjectType

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:19 오후
 */
data class CreateProjectResponse(
    val id: Long,
    val name: String,
    val domain: String,
    val type: ProjectType,
    val status: ProjectStatus,
) {
    companion object {
        fun mapTo(project: Project): CreateProjectResponse {
            return CreateProjectResponse(
                id = project.id,
                name = project.name,
                domain = project.domain,
                type = project.type,
                status = project.status
            )
        }
    }
}