package me.whypie.domain.model.dto.response.project

import com.fasterxml.jackson.annotation.JsonFormat
import me.whypie.domain.model.dto.response.member.MemberProfileResponse
import me.whypie.domain.model.entity.project.Project
import me.whypie.domain.model.entity.project.ProjectStatus
import me.whypie.domain.model.entity.project.ProjectType
import java.time.LocalDateTime

/**
 * @author : choi-ys
 * @date : 2021-10-14 오전 5:14
 */
data class ProjectResponse(
    val id: Long,
    val name: String,
    val domain: String,
    val type: ProjectType,
    val status: ProjectStatus,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,

    val creator: MemberProfileResponse,
) {
    companion object {
        fun mapTo(project: Project): ProjectResponse {
            return ProjectResponse(
                id = project.id,
                name = project.name,
                domain = project.domain,
                type = project.type,
                status = project.status,
                createdAt = project.createdAt,
                updatedAt = project.updatedAt,
                creator = MemberProfileResponse.mapTo(project.member)
            )
        }
    }
}
