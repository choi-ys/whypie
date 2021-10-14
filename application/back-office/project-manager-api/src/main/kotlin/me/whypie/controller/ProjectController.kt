package me.whypie.controller

import me.whypie.model.CurrentUser
import me.whypie.model.LoginUser
import me.whypie.model.dto.request.CreateProjectRequest
import me.whypie.service.ProjectService
import me.whypie.utils.page.PageUtils
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping
    fun findAllByMemberId(
        @RequestParam memberId: Long,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
    ): ResponseEntity<*> {
        return ResponseEntity.ok(projectService.findAllByMemberId(memberId, PageUtils.pageNumberToIndex(pageable)))
    }

    // TODO: 용석(2021-10-14) findById, updateProject, updateProjectStatus
}