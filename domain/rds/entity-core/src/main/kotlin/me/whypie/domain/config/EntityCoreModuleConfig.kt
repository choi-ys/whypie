package me.whypie.domain.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * @author : choi-ys
 * @date : 2021/10/14 12:43 오후
 */
@Configuration
@ComponentScan("me.whypie.domain")
@EnableJpaRepositories(basePackages = ["me.whypie.domain.repository"])
@EntityScan(basePackages = ["me.whypie.domain.model.entity"])
class EntityCoreModuleConfig {
}