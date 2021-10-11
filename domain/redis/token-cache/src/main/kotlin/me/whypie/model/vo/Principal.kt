package me.whypie.model.vo

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * @author : choi-ys
 * @date : 2021-10-10 오전 3:43
 */
data class Principal(
    var identifier: String,
    var authorities: Set<SimpleGrantedAuthority>,
) : User(identifier, "", authorities)