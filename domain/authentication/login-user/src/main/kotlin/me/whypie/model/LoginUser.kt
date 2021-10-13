package me.whypie.model

import org.springframework.security.core.GrantedAuthority

/**
 * @author : choi-ys
 * @date : 2021/10/12 11:38 오전
 */
data class LoginUser(
    val email: String,
    var authorities: Collection<GrantedAuthority?>? = null,
)