package me.whypie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * @author : choi-ys
 * @date : 2021/10/05 11:51 오전
 */
@SpringBootApplication(scanBasePackages = ["me.whypie"])
class ProjectManagerApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(ProjectManagerApplication::class.java)
        .properties(
            "spring.config.location=" +
                    "classpath:application-${System.getProperty("spring.profiles.active")}.yml," +
                    "classpath:rds-${System.getProperty("spring.profiles.active")}.yml," +
                    "classpath:redis-${System.getProperty("spring.profiles.active")}.yml"
        ).run(*args)
}