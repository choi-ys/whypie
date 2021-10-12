package me.whypie.model

import org.springframework.security.core.annotation.AuthenticationPrincipal
import java.lang.annotation.ElementType
import java.lang.annotation.Target

/**
 * @author : choi-ys
 * @date : 2021/10/12 1:02 오후
 */
@Target(ElementType.PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "#this =='anonymousUser' ? null : loginUser")
annotation class CurrentUser
