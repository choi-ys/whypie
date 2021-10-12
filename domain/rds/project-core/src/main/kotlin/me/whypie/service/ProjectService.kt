package me.whypie.service

import me.whypie.model.dto.request.CreateProjectRequest
import me.whypie.model.dto.response.CreateProjectResponse
import me.whypie.repository.ProjectRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021/10/12 4:02 오후
 */
@Service
@Transactional(readOnly = true)
class ProjectService(
    private val projectRepo: ProjectRepo,
) {

    @Transactional
    fun create(createProjectRequest: CreateProjectRequest): CreateProjectResponse {
        if (projectRepo.existsByName(createProjectRequest.name)) {
            throw IllegalArgumentException("")
        }
        val savedProject = projectRepo.save(createProjectRequest.toEntity())
        return CreateProjectResponse.mapTo(savedProject)
    }

}