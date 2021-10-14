package me.whypie.service

import me.whypie.model.LoginUser
import me.whypie.model.dto.request.CreateProjectRequest
import me.whypie.model.dto.response.CreateProjectResponse
import me.whypie.model.dto.response.ProjectResponse
import me.whypie.model.dto.response.page.PageResponse
import me.whypie.model.entity.project.Project
import me.whypie.repository.MemberRepo
import me.whypie.repository.ProjectRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021/10/12 4:02 오후
 */
@Service
@Transactional(readOnly = true)
class ProjectService(
    private val projectRepo: ProjectRepo,
    private val memberRepo: MemberRepo,
) {
    @Transactional
    fun create(createProjectRequest: CreateProjectRequest, loginUser: LoginUser): CreateProjectResponse {
        val member = memberRepo.findByEmail(loginUser.email).orElseThrow() {
            throw IllegalArgumentException("")
        }
        if (projectRepo.existsByName(createProjectRequest.name)) {
            throw IllegalArgumentException("")
        }
        val savedProject = projectRepo.save(createProjectRequest.toEntity(member))
        return CreateProjectResponse.mapTo(savedProject)
    }

    fun findAllByMemberId(memberId: Long, pageable: Pageable): PageResponse {
        val page = projectRepo.findAllByMemberId(memberId, pageable)
        return PageResponse.mapTo(page = page, embedded = mapTo(page))
    }

    fun findById(id: Long): ProjectResponse {
        val project = projectRepo.findById(id).orElseThrow() {
            throw IllegalArgumentException("")
        }
        return ProjectResponse.mapTo(project)
    }

    private fun mapTo(page: Page<Project>): MutableList<ProjectResponse> {
        return page.content.stream()
            .map { ProjectResponse.mapTo(it) }
            .collect(Collectors.toList())
    }
}