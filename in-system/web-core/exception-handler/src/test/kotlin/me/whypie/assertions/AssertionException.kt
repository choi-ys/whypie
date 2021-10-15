package me.whypie.assertions

import me.whypie.error.ErrorCode
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author : choi-ys
 * @date : 2021/10/15 11:32 오전
 */
interface AssertionException {
    companion object {
        fun assertHttpMessageNotReadable(resultActions: ResultActions) {
            resultActions.andDo(print())
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("code").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.name))
                .andExpect(jsonPath("message").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.message))
                .andExpect(jsonPath("method").isNotEmpty)
                .andExpect(jsonPath("path").isNotEmpty)
        }

        fun assertMethodArgumentNotValid(resultActions: ResultActions) {
            resultActions.andDo(print())
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("code").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.name))
                .andExpect(jsonPath("message").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.message))
                .andExpect(jsonPath("method").isNotEmpty)
                .andExpect(jsonPath("path").isNotEmpty)
                .andExpect(jsonPath("$.errorDetails[*].objectName").isNotEmpty)
                .andExpect(jsonPath("$.errorDetails[*].field").isNotEmpty)
                .andExpect(jsonPath("$.errorDetails[*].code").isNotEmpty)
                .andExpect(jsonPath("$.errorDetails[*].rejectMessage").isNotEmpty)
                .andExpect(jsonPath("$.errorDetails[*].rejectedValue").isNotEmpty)
        }
    }
}