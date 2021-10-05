package me.whypie.error

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import me.whypie.error.format.ErrorResourcePropertiesInterface
import org.springframework.validation.FieldError
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:48 오후
 */
data class MethodArgumentNotValidErrorResource(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    override var timestamp: LocalDateTime = LocalDateTime.now(),

    @JsonInclude(JsonInclude.Include.NON_NULL)
    override val code: String,
    override val message: String,

    override val method: String,
    override val path: String,
    private val errorDetails: List<ErrorDetail>? = null,

) : ErrorResourcePropertiesInterface {
    companion object {
        fun create(
            errorCode: ErrorCode,
            httpServletRequest: HttpServletRequest,
            fieldError: List<FieldError>,
        ): MethodArgumentNotValidErrorResource {
            return MethodArgumentNotValidErrorResource(
                code = errorCode.name,
                message = errorCode.message,
                method = httpServletRequest.method,
                path = httpServletRequest.requestURI,
                timestamp = LocalDateTime.now(),
                errorDetails = ErrorDetail.mapTo(fieldError)
            )
        }
    }
}