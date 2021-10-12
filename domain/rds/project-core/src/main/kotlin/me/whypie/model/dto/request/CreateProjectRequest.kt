package me.whypie.model.dto.request

import me.whypie.model.entity.Project
import me.whypie.model.entity.ProjectType

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:19 오후
 */
data class CreateProjectRequest(
    val name: String,
    val domain: String,
    val type: ProjectType,
) {
    fun toEntity(): Project {
        return Project(name = name, domain = domain, type = type)
    }
}