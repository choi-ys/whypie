package me.whypie.advice

import me.whypie.error.ErrorCode
import me.whypie.error.ErrorResource
import me.whypie.error.MethodArgumentNotValidErrorResource
import me.whypie.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.servlet.http.HttpServletRequest

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:46 오후
 */
@RestControllerAdvice
class ClientExceptionAdvice {

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 없는 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(
        exception: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .badRequest()
            .body<Any>(ErrorResource.create(ErrorCode.HTTP_MESSAGE_NOT_READABLE, request))
    }

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 잘못된 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .badRequest()
            .body<Any>(MethodArgumentNotValidErrorResource.create(
                ErrorCode.METHOD_ARGUMENT_NOT_VALID,
                request,
                exception.fieldErrors)
            )
    }

    // [400] @RequestParam, @PathVariable 요청값의 자료형이 잘못된 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun methodArgumentTypeMismatchException(
        exception: MethodArgumentTypeMismatchException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .badRequest()
            .body<Any>(ErrorResource.create(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH, request))
    }

    // [401] 유효한 자격 증명이 아닌 접근인 경우, Unauthorized Exception
    @ExceptionHandler(AuthenticationException::class)
    fun authenticationException(exception: Exception, request: HttpServletRequest): ResponseEntity<*> {
        val errorResource = when (exception) {
            is AuthenticationCredentialsNotFoundException -> {
                ErrorResource.create(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND, request, exception.message)
            }
            is BadCredentialsException -> {
                ErrorResource.create(ErrorCode.BAD_CREDENTIALS, request, exception.message)
            }
            else -> {
                ErrorResource.create(ErrorCode.UNAUTHORIZED, request)
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<Any>(errorResource)
    }

    // [403] 리소스 접근에 필요한 권한이 존재 하지 않는 경우, Unauthorized Exception
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun accessDeniedException(
        exception: AccessDeniedException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body<Any>(ErrorResource.create(ErrorCode.ACCESS_DENIED, request, exception.message))
    }

    // [404] 요청에 해당하는 자원이 존재하지 않는 경우
    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundException(
        exception: ResourceNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body<Any>(ErrorResource.create(ErrorCode.RESOURCE_NOT_FOUND, request))
    }

    // [405] 허용하지 않는 Http Method 요청인 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedException(
        exception: HttpRequestMethodNotSupportedException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body<Any>(ErrorResource.create(ErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED, request))
    }

    // [406] 요청 Accept Type이 잘못된 경우
    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun httpMediaTypeNotAcceptableException(
        exception: HttpMediaTypeNotAcceptableException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body<Any>(ErrorResource.create(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE, request))
    }

    // [415] 요청 Media Type이 잘못된 경우
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun httpMediaTypeNotSupportedException(
        exception: HttpMediaTypeNotSupportedException,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body<Any>(ErrorResource.create(ErrorCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED, request))
    }
}