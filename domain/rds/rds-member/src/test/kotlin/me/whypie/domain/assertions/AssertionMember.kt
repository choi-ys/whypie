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
        fun assertMember(entity: Member, given: Member) {
            Assertions.assertAll(
                { assertDateAuditor(entity) },
                { assertEquals(entity.id, given.id, MUST_EXIST_PK) },
                { assertEquals(entity.email, given.email, MUST_EQUALS) },
                { assertEquals(entity.name, given.name, MUST_EQUALS) },
                { assertEquals(entity.nickname, given.nickname, MUST_EQUALS) },
                { assertEquals(entity.roles, given.roles, MUST_EXIST) },
            )
        }
    }
}