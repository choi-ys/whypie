package me.whypie.domain.model.dto.request.project

import me.whypie.domain.model.entity.member.Member
import me.whypie.domain.model.entity.project.Project
import me.whypie.domain.model.entity.project.ProjectType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:19 오후
 */
data class CreateProjectRequest(
    @field:NotBlank(message = "이름은 필수 입력사항 입니다.")
    @field:Size(min = 1, max = 10, message = "1~10자 이내로 입력하세요.")
    val name: String,

    @field:NotBlank(message = "도메인은 필수 입력사항 입니다.")
    @field:Size(min = 5, max = 30, message = "5~30자 이내로 입력하세요.")
    val domain: String,

    val type: ProjectType,
) {
    fun toEntity(member: Member): Project {
        return Project(name = name, domain = domain, type = type, member = member)
    }
}