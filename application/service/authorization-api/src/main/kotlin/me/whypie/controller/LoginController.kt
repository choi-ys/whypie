package me.whypie.controller

import me.whypie.component.TokenVerifier
import me.whypie.model.dto.request.LoginRequest
import me.whypie.service.LoginService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * @author : choi-ys
 * @date : 2021-10-11 오전 5:42
 */
@RestController
@RequestMapping(
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class LoginController(
    private val loginService: LoginService,
) {

    @PostMapping("login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> =
        ResponseEntity.ok(loginService.login(loginRequest))

    @PostMapping("logout")
    fun logout(httpServletRequest: HttpServletRequest): ResponseEntity<*>? {
        loginService.logout(TokenVerifier.resolve(httpServletRequest))
        return ResponseEntity.noContent().build<Any>()
    }
}