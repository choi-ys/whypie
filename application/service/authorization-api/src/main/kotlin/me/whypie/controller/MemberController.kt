package me.whypie.controller

import me.whypie.domain.model.dto.request.member.SignupRequest
import me.whypie.domain.service.MemberService
import me.whypie.model.CurrentUser
import me.whypie.model.LoginUser
import me.whypie.model.dto.request.CertificationVerifyRequest
import me.whypie.service.MemberCertificationVerifyService
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
    private val memberCertificationVerifyService: MemberCertificationVerifyService,
) {

    @PostMapping
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> =
        ResponseEntity.ok(memberService.signup(signupRequest))

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long) =
        ResponseEntity.ok(memberService.findById(id))

    @GetMapping("me")
    fun me(@CurrentUser loginUser: LoginUser) =
        ResponseEntity.ok(memberService.me(loginUser))

    @PostMapping("send/certification")
    fun sendCertify(@CurrentUser loginUser: LoginUser) {
        memberCertificationVerifyService.sendCertificationMail(loginUser)
    }

    @PostMapping("verify/certification")
    fun verifyCertificationNumber(
        @Valid @RequestBody certificationVerifyRequest: CertificationVerifyRequest,
        @CurrentUser loginUser: LoginUser,
    ) {
        memberCertificationVerifyService.verifyCertification(certificationVerifyRequest, loginUser)
    }
}