package me.whypie.error

import org.springframework.validation.FieldError
import java.util.stream.Collectors

/**
 * @author : choi-ys
 * @date : 2021/10/05 5:48 오후
 */
data class ErrorDetail(
    private var objectName: String,
    private val field: String,
    private val code: String? = null,
    private val rejectMessage: String? = null,
    private val rejectedValue: Any? = null,
) {
    companion object {
        fun mapTo(fieldErrors: List<FieldError>): List<ErrorDetail> {
            return fieldErrors.stream()
                .map {
                    ErrorDetail(
                        objectName = it.objectName,
                        field = it.field,
                        code = it.code,
                        rejectMessage = it.defaultMessage,
                        rejectedValue = it.rejectedValue
                    )
                }
                .collect(Collectors.toList())
        }
    }
}