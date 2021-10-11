package me.whypie.config

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 9:07
 */
class P6spySqlFormatConfiguration : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String
    ): String {
        var formattedQuery: String? = formatSql(category, sql)
        return "\n -> [Meta info] : " +
                LocalDateTime.ofInstant(Instant.ofEpochMilli(now.toLong()), TimeZone.getDefault().toZoneId()) +
                " | duration " + elapsed + "ms | connection " + connectionId + formattedQuery
    }

    private fun formatSql(category: String, sql: String?): String? {
        if (sql == null || sql.trim { it <= ' ' } == "") return sql
        var formattedQuery = sql

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.name == category) {
            val tmpsql = formattedQuery.trim { it <= ' ' }.lowercase(Locale.ROOT)
            formattedQuery = if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                FormatStyle.DDL.formatter.format(formattedQuery)
            } else {
                FormatStyle.BASIC.formatter.format(formattedQuery)
            }
            formattedQuery = "\n -> [Hibernate format]: $formattedQuery"
        }
        return formattedQuery
    }
}