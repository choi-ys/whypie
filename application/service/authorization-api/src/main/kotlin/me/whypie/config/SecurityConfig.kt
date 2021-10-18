package me.whypie.config

import me.whypie.component.TokenVerifier
import me.whypie.domain.model.entity.member.MemberRole
import me.whypie.filter.JwtConfigurer
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * @author : choi-ys
 * @date : 2021-10-11 오전 4:05
 */
@EnableWebSecurity
class SecurityConfig(
    private val tokenVerifier: TokenVerifier,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .apply(JwtConfigurer(tokenVerifier))
            .and()

            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .authorizeRequests {
                it
                    .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/member/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/member/me").hasRole(MemberRole.CERTIFIED_MEMBER.name)
                    .antMatchers(HttpMethod.POST, "/member", "/login", "/logout").permitAll()
                    .antMatchers(HttpMethod.POST, "/auth/token", "/auth/refresh").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/auth/expire").permitAll()
                    .anyRequest().authenticated()
            }

    }
}