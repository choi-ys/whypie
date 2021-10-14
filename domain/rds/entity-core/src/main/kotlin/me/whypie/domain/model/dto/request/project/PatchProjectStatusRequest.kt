package me.whypie.domain.model.dto.request.project

import me.whypie.domain.model.entity.project.ProjectStatus

/**
 * @author : choi-ys
 * @date : 2021/10/14 2:53 오후
 */
data class PatchProjectStatusRequest(
    val status: ProjectStatus
)