package me.whypie.config

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.web.filter.CharacterEncodingFilter

/**
 * @author : choi-ys
 * @date : 2021/09/02 8:41 오후
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@AutoConfigureMockMvc
@Import(
    EnableMockMvc.Config::class
)
annotation class EnableMockMvc {
    class Config {
        @Bean
        fun characterEncodingFilter(): CharacterEncodingFilter {
            return CharacterEncodingFilter("UTF-8", true)
        }
    }
}