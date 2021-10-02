package me.whypie.config

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration
import me.whypie.domain.config.DataJpaConfig
import me.whypie.domain.config.P6spyLogMessageFormatConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor
import javax.persistence.EntityManager

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 11:29
 */
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration::class)
@Import(P6spyLogMessageFormatConfiguration::class, DataJpaConfig::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DataJpaTestConfig {

    @Autowired
    lateinit var entityManger: EntityManager

    fun flush() = entityManger.flush()

    fun clear() = entityManger.clear()

    fun flushAndClear() {
        flush()
        clear()
    }
}