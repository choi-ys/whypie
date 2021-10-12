package me.whypie.model.dto.request

import me.whypie.model.entity.member.Member
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:40
 */
data class SignupRequest(

    @field:Email(message = "옳바른 이메일 형식이 아닙니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력사항 입니다.")
    @field:Size(min = 8, max = 20, message = "8~20자 이내로 입력하세요.")
    val password: String,

    @field:NotBlank(message = "이름은 필수 입력사항 입니다.")
    @field:Size(min = 1, max = 10, message = "1~10자 이내로 입력하세요.")
    val name: String,

    @field:NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    @field:Size(min = 1, max = 10, message = "1~10자 이내로 입력하세요")
    val nickname: String,
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        return Member(email = email, password = passwordEncoder.encode(password), name = name, nickname = nickname)
    }
}