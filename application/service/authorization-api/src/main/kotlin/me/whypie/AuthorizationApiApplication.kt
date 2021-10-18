package me.whypie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 * @author : choi-ys
 * @date : 2021-10-08 오후 11:21
 */
@SpringBootApplication
@EnableDiscoveryClient
class AuthorizationApiApplication

fun main(args: Array<String>) {
    val profiles = System.getProperty("spring.profiles.active")
    SpringApplicationBuilder(AuthorizationApiApplication::class.java)
        .properties(
            "spring.config.location=" +
                    "classpath:application-${profiles}.yml," +
                    "classpath:jwt-${profiles}.yml," +
                    "classpath:rds-${profiles}.yml," +
                    "classpath:redis-${profiles}.yml"
        ).run(*args)
}