package me.whypie.model.entity

import me.whypie.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 11:01
 */
@DisplayName("RDS:Entity:Member")
internal class MemberTest {

    @Test
    @DisplayName("회원 Entity 객체 생성")
    internal fun create() {
        // Given
        val email = "project.log.062@gmail.com"
        val password = "password"
        val name = "choi-ys"
        val nickname = "whypie"

        // When
        val member = Member(email = email, password = password, name = name, nickname = nickname)

        // Then
        assertAll(
            { assertEquals(member.id, 0L, "객체 생성 시 id 항목의 기본 값 '0' 여부 확인") },
            { assertEquals(member.email, email, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(member.password, password, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(member.name, name, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(member.nickname, nickname, "입력 파라미터 항목의 일치 여부 확인") },
            {
                assertEquals(
                    member.roles,
                    setOf(MemberRole.UNCERTIFIED_MEMBER),
                    "객체 생성 시 roles 항목의 기본값 'UNCERTIFIED_MEMBER' 여부 확인"
                )
            },
            { assertNull(member.createdBy, "rds-common 모듈의 Auditor를 통해 설정되는 생성주체 정보의 null 여부 확인") },
            { assertNull(member.createdAt, "rds-common 모듈의 Auditor를 통해 설정되는 생성일자 정보의 null 여부 확인") },
            { assertNull(member.updatedBy, "rds-common 모듈의 Auditor를 통해 설정되는 수정주체 정보의 null 여부 확인") },
            { assertNull(member.updatedAt, "rds-common 모듈의 Auditor를 통해 설정되는 수정일시 정보의 null 여부 확인") },
        )
    }

    @Test
    @DisplayName("회원 권한 추가")
    fun addRoles() {
        // Given
        val member = MemberGenerator.member()
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        member.addRoles(additionRoles)

        // Then
        assertTrue(member.roles.containsAll(additionRoles))
    }

    @Test
    @DisplayName("회원 권한 제거")
    fun removeRoles() {
        // Given
        val member = MemberGenerator.member()
        member.addRoles(setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN))
        val removalRoles = setOf(MemberRole.UNCERTIFIED_MEMBER, MemberRole.ADMIN)

        // When
        member.removeRoles(removalRoles)

        // Then
        assertFalse(member.roles.containsAll(removalRoles))
    }

    @Test
    @DisplayName("모든 권한 제거 시 예외")
    fun exceptionByRemoveAllRoles() {
        // Given
        val member = MemberGenerator.member()

        // When
        val exception = assertThrows(IllegalArgumentException::class.java) {
            member.removeRoles(member.roles)
        }

        // Then
        org.junit.jupiter.api.assertAll(
            { assertEquals(exception.javaClass.simpleName, IllegalArgumentException::class.simpleName) },
            { assertEquals(exception.message, "최소 하나 이상의 권한이 존재해야 합니다.") }
        )
    }
}