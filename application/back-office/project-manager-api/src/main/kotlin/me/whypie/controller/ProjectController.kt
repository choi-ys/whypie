package me.whypie.controller

import me.whypie.model.CurrentUser
import me.whypie.model.LoginUser
import me.whypie.model.dto.request.CreateProjectRequest
import me.whypie.service.ProjectService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author : choi-ys
 * @date : 2021-10-13 오후 3:27
 */
@RestController
@RequestMapping(
    value = ["project"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class ProjectController(
    private val projectService: ProjectService,
) {

    @PostMapping
    fun create(
        @Valid @RequestBody createProjectRequest: CreateProjectRequest,
        @CurrentUser loginUser: LoginUser,
    ): ResponseEntity<*> = ResponseEntity.ok(projectService.create(createProjectRequest, loginUser))
}