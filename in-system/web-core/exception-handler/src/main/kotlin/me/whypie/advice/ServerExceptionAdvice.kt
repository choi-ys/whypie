package me.whypie.advice

import me.whypie.error.ErrorCode
import me.whypie.error.ErrorResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:46 오후
 */
// TODO local gradle build시, HttpMessageNotReadableException이 RuntimeException Handler로 catch되어 TC 실패 하는 현상
//@RestControllerAdvice
class ServerExceptionAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(exception: RuntimeException, request: HttpServletRequest): ResponseEntity<*>? {
        exception.printStackTrace()
        return ResponseEntity.internalServerError()
            .body<Any>(ErrorResource.create(ErrorCode.SERVER_ERROR, request))
    }
}