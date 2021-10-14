package me.whypie.domain.service

import me.whypie.domain.model.dto.request.member.SignupRequest
import me.whypie.domain.model.dto.response.member.MemberProfileResponse
import me.whypie.domain.repository.MemberRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:37
 */
@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepo: MemberRepo,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signup(signupRequest: SignupRequest): MemberProfileResponse {
        memberRepo.existsByEmail(signupRequest.email).let {
            if (it) throw IllegalArgumentException("이미 존재하는 이메일 입니다.")
        }

        val savedMember = memberRepo.save(signupRequest.toEntity(passwordEncoder))
        return MemberProfileResponse.mapTo(savedMember)
    }
}