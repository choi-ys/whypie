package me.whypie.domain.redis.common.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.util.StringUtils
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

private val logger = KotlinLogging.logger { }

/**
 * @author : choi-ys
 * @date : 2021/09/24 12:53 오전
 */
@Configuration
@Profile(value = ["test", "local"])
class EmbeddedRedisConfig {
    @Value("\${spring.redis.port}")
    private val redisPort = 0
    private var redisServer: RedisServer? = null

    @PostConstruct
    @Throws(IOException::class)
    fun redisServer() {
        var port = if (isRedisRunning()) findAvailablePort() else redisPort
        port = 6379
        redisServer = RedisServer(port)
        try {
            redisServer!!.start()
        } catch (e: Exception) {
            port = findAvailablePort()
            redisServer = RedisServer(port)
        }
        logger.info("[Embedded Redis ::] Start on [{}] by [{}] port", System.getProperty("os.name"), port);
    }

    @PreDestroy
    fun stopRedis() {
        if (redisServer != null) {
            redisServer!!.stop()
        }
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
//    @get:Throws(IOException::class)
//    private val isRedisRunning: Boolean = isRunning(executeGrepProcessCommand(redisPort))
    private fun isRedisRunning(): Boolean = isRunning(executeGrepProcessCommand(redisPort))

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    @Throws(IOException::class)
    fun findAvailablePort(): Int {
        for (port in 10000..65535) {
            val process = executeGrepProcessCommand(port)
            if (!isRunning(process)) {
                logger.debug("[Embedded Redis ::] Running by [{}] port... find not using [{}] port", redisPort, port);
                return port
            }
        }
        throw IllegalArgumentException("[Embedded Redis ::] Not Found Available port: 10000 ~ 65535")
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    @Throws(IOException::class)
    private fun executeGrepProcessCommand(port: Int): Process {
        val exec = Runtime.getRuntime().exec(osShellCommand(port))
        logger.info("[Embedded Redis ::] Port [{}] status is : {}", port, exec)
        return exec
    }

    /**
     * OS별 수행 shell command 반환
     *
     * @param port 현재 구동 환경에서 점유되지 않아 사용 가능한 port
     */
    fun osShellCommand(port: Int): Array<String> {
        val command = when (findOS()) {
            OS.WINDOWS -> OS.WINDOWS.mapTo(port)
            OS.OTHERS -> OS.OTHERS.mapTo(port)
        }
        logger.info("[Embedded Redis ::] OS is {}, execute command : {}", findOS(), command.contentToString())
        return command
    }

    /**
     * 현재 구동중인 OS 반환
     */
    fun findOS(): OS {
        val os = System.getProperty("os.name")
        return if (os.lowercase().contains(OS.WINDOWS.name.lowercase())) OS.WINDOWS else OS.OTHERS
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private fun isRunning(process: Process): Boolean {
        var line: String? = null
        val pidInfo = StringBuilder()
        try {
            BufferedReader(InputStreamReader(process.inputStream)).use { input ->
                while (input.readLine().also { line = it } != null) {
                    pidInfo.append(line)
                }
            }
        } catch (e: Exception) {
        }
        logger.debug(
            "[Embedded Redis ::] Pid info [{}] by execute command [{}], check isRunning -> {}",
            pidInfo, line, !StringUtils.hasText(pidInfo.toString())
        );
        return StringUtils.hasText(pidInfo.toString())
    }
}