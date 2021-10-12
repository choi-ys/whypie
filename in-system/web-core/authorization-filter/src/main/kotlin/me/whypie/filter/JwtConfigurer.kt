package me.whypie.filter

import me.whypie.component.TokenVerifier
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

/**
 * @author : choi-ys
 * @date : 2021/10/12 11:33 오전
 */
class JwtConfigurer(
    private val tokenVerifier: TokenVerifier,
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterAt(
            JwtFilter(tokenVerifier),
            BasicAuthenticationFilter::class.java
        )
    }
}
