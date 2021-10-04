package me.whypie.domain.rds.common.config

import me.whypie.domain.rds.common.model.entity.base.AuditorWAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 8:54
 */
@Configuration
@EnableJpaAuditing
class DataJpaConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorWAwareImpl()
    }
}