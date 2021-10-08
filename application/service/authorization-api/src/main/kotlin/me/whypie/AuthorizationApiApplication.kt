package me.whypie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author : choi-ys
 * @date : 2021-10-08 오후 11:21
 */
@SpringBootApplication
class AuthorizationApiApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationApiApplication>(*args)
}