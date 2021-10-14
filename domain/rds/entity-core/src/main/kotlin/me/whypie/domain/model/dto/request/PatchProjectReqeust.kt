package me.whypie.domain.model.dto.request

import me.whypie.domain.model.entity.project.ProjectType

/**
 * @author : choi-ys
 * @date : 2021/10/14 4:02 오후
 */
data class PatchProjectRequest(
    val name: String,
    val domain: String,
    val type: ProjectType,
)