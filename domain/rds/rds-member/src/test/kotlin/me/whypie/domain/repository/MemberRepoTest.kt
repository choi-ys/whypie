package me.whypie.domain.repository

import me.whypie.config.DataJpaTestConfig
import me.whypie.domain.model.entity.Member
import me.whypie.domain.model.entity.MemberRole
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 11:31
 */
@DisplayName("RDS:Repo:Member")
class MemberRepoTest(
    val memberRepo: MemberRepo
) : DataJpaTestConfig() {

    @Test
    @DisplayName("Member Entity 저장")
    internal fun save() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "최용석"
        val nickname = "whypie"
        val member = Member(email = email, password = password, name = name, nickname = nickname)

        // When
        val expected = memberRepo.save(member)

        // Then
        assertAll(
            { assertNotEquals(expected.id, 0L, "Persist 상태의 Entity 객체 id의 값 generated 여부 확인") },
            { assertEquals(expected.email, email, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.password, password, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.name, name, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.nickname, nickname, "입력 파라미터 항목의 일치 여부 확인") },
            {
                assertEquals(
                    expected.roles,
                    setOf(MemberRole.UNCERTIFIED_MEMBER),
                    "객체 생성 시 roles 항목의 기본값 'UNCERTIFIED_MEMBER' 여부 확인"
                )
            },
            { assertNull(expected.createdBy, "rds-common 모듈의 Auditor를 통해 설정되는 생성주체 정보의 null 여부 확인") },
            { assertNotNull(expected.createdAt, "rds-common 모듈의 Auditor를 통해 설정되는 생성일자 정보의 not null 여부 확인") },
            { assertNull(expected.updatedBy, "rds-common 모듈의 Auditor를 통해 설정되는 생성주체 정보의 null 여부 확인") },
            { assertNotNull(expected.updatedAt, "rds-common 모듈의 Auditor를 통해 설정되는 수정일자 정보의 not null 여부 확인") },
        )
    }
}