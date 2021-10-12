package me.whypie.model.vo

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021-10-10 오전 3:43
 */
data class Principal(
    var identifier: String,
    var authorities: String,
) {
    fun convert(): User {
        return User(
            identifier,
            "",
            authorities.split(",").stream()
                .map { SimpleGrantedAuthority(it) }
                .collect(Collectors.toSet())
        )
    }

    companion object {
        fun <T> mapTo(identifier: String, set: Set<T>): Principal {
            return Principal(identifier, set.joinToString(","))
        }
    }
}