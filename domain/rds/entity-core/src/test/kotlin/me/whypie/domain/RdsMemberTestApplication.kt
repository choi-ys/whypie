package me.whypie.domain

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 11:09
 */
@SpringBootApplication(scanBasePackages = ["me.whypie.domain.config"])
class RdsMemberTestApplication

fun main(args: Array<String>) {
    SpringApplication.run(RdsMemberTestApplication::class.java, *args)

    @Configuration
    @EntityScan(basePackages = ["me.whypie.domain.model.entity"])
    @EnableJpaRepositories(basePackages = ["me.whypie.domain.repository"])
    class JpaConfig
}