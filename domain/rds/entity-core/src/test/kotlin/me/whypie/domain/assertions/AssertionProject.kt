package me.whypie.domain.assertions

import me.whypie.assertions.AssertionEntity.Companion.MUST_EQUALS
import me.whypie.assertions.AssertionEntity.Companion.MUST_EXIST
import me.whypie.assertions.AssertionEntity.Companion.MUST_EXIST_PK
import me.whypie.assertions.AssertionEntity.Companion.assertDateAuditor
import me.whypie.domain.model.entity.project.Project
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:01
 */
interface AssertionProject {
    companion object {
        fun assertProject(expected: Project, given: Project) {
            Assertions.assertAll(
                { assertDateAuditor(expected) },
                { assertEquals(expected.id, given.id, MUST_EXIST_PK) },
                { assertEquals(expected.name, given.name, MUST_EQUALS) },
                { assertEquals(expected.domain, given.domain, MUST_EQUALS) },
                { assertEquals(expected.type, given.type, MUST_EQUALS) },
                { assertEquals(expected.status, given.status, MUST_EXIST) },
            )
        }
    }
}