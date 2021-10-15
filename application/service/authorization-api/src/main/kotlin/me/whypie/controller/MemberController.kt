package me.whypie.controller

import me.whypie.domain.model.dto.request.member.SignupRequest
import me.whypie.domain.service.MemberService
import me.whypie.model.CurrentUser
import me.whypie.model.LoginUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author : choi-ys
 * @date : 2021-10-11 오전 5:33
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

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long) =
        ResponseEntity.ok(memberService.findById(id))

}