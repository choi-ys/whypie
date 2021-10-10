package me.whypie.controller

import me.whypie.component.TokenVerifier
import me.whypie.model.vo.Principal
import me.whypie.service.AuthorizationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021-10-10 오후 7:07
 */
@RestController
@RequestMapping(
    value = ["auth"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AuthorizationController(
    private val authorizationService: AuthorizationService
) {

    @PostMapping("token")
    fun issue(@RequestBody principal: Principal): ResponseEntity<*> =
        ResponseEntity.ok(authorizationService.issue(principal))

    @PostMapping("refresh")
    fun refresh(httpServletRequest: HttpServletRequest): ResponseEntity<*> =
        ResponseEntity.ok(authorizationService.refresh(TokenVerifier.resolve(httpServletRequest)))

    @DeleteMapping("expire")
    fun expire(httpServletRequest: HttpServletRequest): ResponseEntity<*>? {
        authorizationService.expire(TokenVerifier.resolve(httpServletRequest))
        return ResponseEntity.noContent().build<Any>()
    }
}