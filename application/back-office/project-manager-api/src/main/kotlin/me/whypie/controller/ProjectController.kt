package me.whypie.controller

import me.whypie.domain.model.dto.request.CreateProjectRequest
import me.whypie.domain.model.dto.request.PatchProjectStatusRequest
import me.whypie.domain.service.ProjectService
import me.whypie.domain.utils.page.PageUtils
import me.whypie.model.CurrentUser
import me.whypie.model.LoginUser
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

    @GetMapping("member")
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

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long) = ResponseEntity.ok(projectService.findById(id))

    @PatchMapping("{id}")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestBody patchProjectStatusRequest: PatchProjectStatusRequest,
        @CurrentUser loginUser: LoginUser,
    ): ResponseEntity<*> {
        ResponseEntity.ok(projectService.updateStatus(id, patchProjectStatusRequest, loginUser))
        return ResponseEntity.ok(projectService.findById(id))
    }


    // TODO: 용석(2021-10-14) findById, updateProject, updateProjectStatus
}