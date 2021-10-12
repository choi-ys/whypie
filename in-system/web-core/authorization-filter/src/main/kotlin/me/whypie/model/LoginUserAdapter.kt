package me.whypie.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * @author : choi-ys
 * @date : 2021/10/12 11:38 오전
 */
class LoginUserAdapter(username: String, authorities: Collection<GrantedAuthority>?) : User(username, "", authorities) {
    val loginUser: LoginUser = LoginUser(username, authorities)
}