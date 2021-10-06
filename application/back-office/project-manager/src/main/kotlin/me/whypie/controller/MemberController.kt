package me.whypie.controller

import me.whypie.model.dto.request.SignupRequest
import me.whypie.service.MemberService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author : choi-ys
 * @date : 2021/10/05 11:34 오전
 */
@RestController
@RequestMapping(
    value = ["member"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class MemberController(
    private val memberService: MemberService,
) {

    @PostMapping
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> =
        ResponseEntity.ok(memberService.signup(signupRequest))
}