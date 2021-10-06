package me.whypie.error

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import me.whypie.error.format.ErrorResourcePropertiesAbstract
import org.springframework.util.StringUtils
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:48 오후
 */
data class ErrorResource(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    override var timestamp: LocalDateTime = LocalDateTime.now(),

    @JsonInclude(JsonInclude.Include.NON_NULL)
    override var code: String,

    override var message: String,

    override var method: String,

    override var path: String,

) : ErrorResourcePropertiesAbstract(code = code, message = message, method = method, path = path) {
    companion object {
        fun create(errorCode: ErrorCode, httpServletRequest: HttpServletRequest): ErrorResource {
            return ErrorResource(
                code = errorCode.name,
                message = errorCode.message,
                method = httpServletRequest.method,
                path = httpServletRequest.requestURI,
                timestamp = LocalDateTime.now()
            )
        }

        fun create(errorCode: ErrorCode, httpServletRequest: HttpServletRequest, message: String?): ErrorResource {
            return ErrorResource(
                code = errorCode.name,
                message = if (StringUtils.hasText(message)) message ?: "" else errorCode.message,
                method = httpServletRequest.method,
                path = httpServletRequest.requestURI,
                timestamp = LocalDateTime.now()
            )
        }
    }
}
