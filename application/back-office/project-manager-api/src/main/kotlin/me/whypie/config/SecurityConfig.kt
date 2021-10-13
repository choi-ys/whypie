package me.whypie.config

import me.whypie.component.TokenVerifier
import me.whypie.filter.JwtConfigurer
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * @author : choi-ys
 * @date : 2021-10-13 오후 7:16
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
                    .antMatchers(HttpMethod.POST, "/project").permitAll()
                    .anyRequest().authenticated()
            }
    }
}