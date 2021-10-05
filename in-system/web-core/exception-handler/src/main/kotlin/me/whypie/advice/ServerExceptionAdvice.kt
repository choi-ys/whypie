package me.whypie.advice

import me.whypie.error.ErrorCode
import me.whypie.error.ErrorResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:46 오후
 */
@RestControllerAdvice
class ServerExceptionAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(exception: RuntimeException, request: HttpServletRequest): ResponseEntity<*>? {
        exception.printStackTrace()
        return ResponseEntity.internalServerError()
            .body<Any>(ErrorResource.create(ErrorCode.SERVER_ERROR, request))
    }
}