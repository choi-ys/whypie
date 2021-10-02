package io.whypie.whypie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WhypieApplication

fun main(args: Array<String>) {
    runApplication<WhypieApplication>(*args)
}
