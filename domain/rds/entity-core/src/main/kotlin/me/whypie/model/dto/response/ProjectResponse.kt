package me.whypie.model.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import me.whypie.model.entity.project.Project
import me.whypie.model.entity.project.ProjectStatus
import me.whypie.model.entity.project.ProjectType
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
    val creator: MemberProfileResponse,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime?,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun mapTo(project: Project): ProjectResponse {
            return ProjectResponse(
                id = project.id,
                name = project.name,
                domain = project.domain,
                type = project.type,
                status = project.status,
                creator = MemberProfileResponse.mapTo(project.member),
                createdAt = project.createdAt,
                updatedAt = project.updatedAt
            )
        }
    }
}
