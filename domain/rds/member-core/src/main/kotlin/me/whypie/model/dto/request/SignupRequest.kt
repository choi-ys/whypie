package me.whypie.model.dto.request

import me.whypie.model.entity.Member
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:40
 */
data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val nickname: String
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        return Member(email = email, password = passwordEncoder.encode(password), name = name, nickname = nickname)
    }
}