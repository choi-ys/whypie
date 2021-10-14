package me.whypie.config

import com.p6spy.engine.spy.P6SpyOptions
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 9:06
 */
@Configuration
class P6spyLogMessageFormatConfiguration {
    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6spySqlFormatConfiguration::class.java.name
    }
}