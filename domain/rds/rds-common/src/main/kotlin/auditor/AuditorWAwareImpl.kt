package me.whypie.domain.model.entity.base.auditor

import org.springframework.data.domain.AuditorAware
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 9:08
 */
class AuditorWAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        val authentication = SecurityContextHolder.getContext().authentication
        return when {
            invalidAuditor(authentication) -> Optional.empty()
            else -> Optional.of(authentication.principal as String)
        }
    }

    private fun invalidAuditor(authentication: Authentication?): Boolean {
        return authentication == null
                || !authentication.isAuthenticated
                || authentication is AnonymousAuthenticationToken
    }
}