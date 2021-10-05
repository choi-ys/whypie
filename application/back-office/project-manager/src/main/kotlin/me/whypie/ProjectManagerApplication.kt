package me.whypie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author : choi-ys
 * @date : 2021/10/05 11:51 오전
 */
@SpringBootApplication
class ProjectManagerApplication

fun main(args: Array<String>) {
    runApplication<ProjectManagerApplication>(*args)
}