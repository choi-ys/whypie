package me.whypie.assertions

import me.whypie.model.entity.base.Auditor
import org.junit.jupiter.api.Assertions
import org.springframework.util.Assert

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 5:26
 */
class AssertionEntity {
    companion object {
        fun <T : Auditor> assertDateAuditor(entity: T) {
            Assert.notNull(entity, "Entity must not be null")
            Assertions.assertAll(
                { Assertions.assertNotNull(entity.createdAt, MUST_EXIST_AUDITOR) },
                { Assertions.assertNotNull(entity.updatedAt, MUST_EXIST_AUDITOR) },
            )
        }

        fun <T : Auditor> assertAuditor(entity: T) {
            assertDateAuditor(entity)
            Assertions.assertAll(
                { Assertions.assertNotNull(entity.createdBy, MUST_EXIST_AUDITOR) },
                { Assertions.assertNotNull(entity.updatedBy, MUST_EXIST_AUDITOR) },
            )
        }

        private const val MUST_EXIST_AUDITOR = "생성/수정 관련 Entity Metadata는 반드시 존재해야 한다."

        const val MUST_EXIST = "해당 항목은 Null이 아닌 값이 반드시 존재해야 한다."
        const val MUST_EXIST_PK = "PK는 반드시 존재해야 한다."
        const val MUST_EQUALS = "해당 항목과 주어진 값은 반드시 같아야 한다."
    }
}