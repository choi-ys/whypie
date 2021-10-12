package me.whypie.filter

import me.whypie.component.TokenVerifier
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021/10/12 11:33 오전
 */
class JwtFilter(
    private val tokenVerifier: TokenVerifier,
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = TokenVerifier.resolve(request as HttpServletRequest)
        if ("" != token) {
            tokenVerifier.verify(token)
            val authentication = tokenVerifier.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain.doFilter(request, response)
        return
    }
}
