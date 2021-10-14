package me.whypie.domain.assertions

import me.whypie.assertions.AssertionEntity.Companion.MUST_EQUALS
import me.whypie.assertions.AssertionEntity.Companion.MUST_EXIST
import me.whypie.assertions.AssertionEntity.Companion.MUST_EXIST_PK
import me.whypie.assertions.AssertionEntity.Companion.assertDateAuditor
import me.whypie.domain.model.entity.project.Project
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:01
 */
interface AssertionProject {
    companion object {
        fun assertEntity(expected: Project, given: Project) {
            Assertions.assertAll(
                { assertDateAuditor(expected) },
                { assertEquals(expected.id, given.id, MUST_EXIST_PK) },
                { assertEquals(expected.name, given.name, MUST_EQUALS) },
                { assertEquals(expected.domain, given.domain, MUST_EQUALS) },
                { assertEquals(expected.type, given.type, MUST_EQUALS) },
                { assertEquals(expected.status, given.status, MUST_EXIST) },
            )
        }

        fun assertDetailResponse(resultActions: ResultActions, given: Project) {
            resultActions
                .andExpect(jsonPath("createdAt").isNotEmpty)
                .andExpect(jsonPath("updatedAt").isNotEmpty)
                .andExpect(jsonPath("creator.id").value(given.member.id))
                .andExpect(jsonPath("creator.email").value(given.member.email))
                .andExpect(jsonPath("creator.name").value(given.member.name))
                .andExpect(jsonPath("creator.nickname").value(given.member.nickname))
        }

        fun assertPageResponse(resultActions: ResultActions) {
            resultActions
                .andExpect(jsonPath("totalPages").exists())
                .andExpect(jsonPath("totalElementCount").exists())
                .andExpect(jsonPath("currentPage").exists())
                .andExpect(jsonPath("currentElementCount").exists())
                .andExpect(jsonPath("perPageNumber").exists())
                .andExpect(jsonPath("firstPage").exists())
                .andExpect(jsonPath("lastPage").exists())
                .andExpect(jsonPath("hasNextPage").exists())
                .andExpect(jsonPath("hasPrevious").exists())
                .andExpect(jsonPath("$.embedded[*].id").exists())
                .andExpect(jsonPath("$.embedded[*].name").exists())
                .andExpect(jsonPath("$.embedded[*].domain").exists())
                .andExpect(jsonPath("$.embedded[*].type").exists())
                .andExpect(jsonPath("$.embedded[*].status").exists())
                .andExpect(jsonPath("$.embedded[*].createdAt").exists())
                .andExpect(jsonPath("$.embedded[*].updatedAt").exists())
                .andExpect(jsonPath("$.embedded[*].creator.id").exists())
                .andExpect(jsonPath("$.embedded[*].creator.email").exists())
                .andExpect(jsonPath("$.embedded[*].creator.name").exists())
                .andExpect(jsonPath("$.embedded[*].creator.nickname").exists())
        }
    }
}