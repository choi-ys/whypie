package me.whypie.domain.assertions

import me.whypie.domain.assertions.AssertionEntity.Companion.MUST_EQUALS
import me.whypie.domain.assertions.AssertionEntity.Companion.MUST_EXIST
import me.whypie.domain.assertions.AssertionEntity.Companion.MUST_EXIST_PK
import me.whypie.domain.assertions.AssertionEntity.Companion.assertDateAuditor
import me.whypie.domain.model.entity.Member
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:01
 */
interface AssertionMember {
    companion object {
        fun assertMember(expected: Member, given: Member) {
            Assertions.assertAll(
                { assertDateAuditor(expected) },
                { assertEquals(expected.id, given.id, MUST_EXIST_PK) },
                { assertEquals(expected.email, given.email, MUST_EQUALS) },
                { assertEquals(expected.name, given.name, MUST_EQUALS) },
                { assertEquals(expected.nickname, given.nickname, MUST_EQUALS) },
                { assertEquals(expected.roles, given.roles, MUST_EXIST) },
            )
        }
    }
}