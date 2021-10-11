package me.whypie.service

import me.whypie.model.dto.request.LoginRequest
import me.whypie.model.dto.response.LoginResponse
import me.whypie.model.vo.Principal
import me.whypie.repository.MemberRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author : choi-ys
 * @date : 2021-10-07 오후 10:21
 */
@Service
class LoginService(
    private val memberRepo: MemberRepo,
    private val passwordEncoder: PasswordEncoder,
    private val authorizationService: AuthorizationService,
) {

    fun login(loginRequest: LoginRequest): LoginResponse {
        val member = memberRepo.findByEmail(loginRequest.email).orElseThrow() {
            throw IllegalArgumentException("")
        }.also {
            if (!passwordEncoder.matches(loginRequest.password, it.password)) {
                throw IllegalArgumentException("")
            }
        }
        val token = authorizationService.issue(Principal(member.email, member.mapToSimpleGrantedAuthority()))
        return LoginResponse.mapTo(member, token)
    }

    fun logout() {
//        authorizationService.expire()
    }
}