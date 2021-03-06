package me.whypie.monitoring

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 * @author : choi-ys
 * @date : 2021/10/18 4:50 오후
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
class SpringBootAdminApplication

fun main(args: Array<String>) {
    runApplication<SpringBootAdminApplication>(* args)
}