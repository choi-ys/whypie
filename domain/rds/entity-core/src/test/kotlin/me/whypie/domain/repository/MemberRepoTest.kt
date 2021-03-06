package me.whypie.domain.repository

import me.whypie.config.DataJpaTestConfig
import me.whypie.domain.assertions.AssertionMember.Companion.assertMember
import me.whypie.domain.generator.MemberGenerator
import me.whypie.domain.model.entity.member.Member
import me.whypie.domain.model.entity.member.MemberRole
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.dao.DataIntegrityViolationException

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 11:31
 */
@DisplayName("RDS:Repo:Member")
class MemberRepoTest(
    val memberRepo: MemberRepo,
) : DataJpaTestConfig() {

    @Test
    @DisplayName("회원 객체 저장")
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

    @Test
    @DisplayName("회원 객체 저장 실패: 중복된 Email")
    fun findByEmail_Fail_Cause_DuplicatedEmail() {
        // Given
        val member = Member(email = "rcn115@naver.com", password = "password", name = "name", nickname = "nickname")
        val duplicatedMember =
            Member(email = "rcn115@naver.com", password = "password", name = "name", nickname = "nickname")

        // When
        memberRepo.save(member)

        // Then
        assertThrows(DataIntegrityViolationException::class.java) {
            memberRepo.save(duplicatedMember)
        }.let {
            assertTrue(it is RuntimeException)
        }
    }

    @Test
    @DisplayName("회원 객체 조회: Id")
    fun findById() {
        // Given
        val savedMember = memberRepo.save(MemberGenerator.member())
        flushAndClear()

        // When
        val expected = memberRepo.findById(savedMember.id).orElseThrow() { throw IllegalArgumentException("") }

        // Then
        assertAll(
            { assertNotEquals(expected.id, 0L, "Persist 상태의 Entity 객체 id의 값 generated 여부 확인") },
            { assertEquals(expected.email, savedMember.email, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.password, savedMember.password, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.name, savedMember.name, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.nickname, savedMember.nickname, "입력 파라미터 항목의 일치 여부 확인") },
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

    @Test
    @DisplayName("회원 객체 조회 실패: 존재 하지 않는 Id")
    fun findByEmail_Fail_Cause_NotExistId() {
        // Given
        val invalidId = 0L

        // When & Then
        assertThrows(NoSuchElementException::class.java) {
            memberRepo.findById(invalidId).orElseThrow()
        }.let {
            assertTrue(it is RuntimeException)
        }
    }

    @Test
    @DisplayName("회원 객체 조회: Email")
    fun findByEmail() {
        // Given
        val savedMember = memberRepo.save(MemberGenerator.member())
        flushAndClear()

        // When
        val expected = memberRepo.findByEmail(savedMember.email).orElseThrow() { throw IllegalArgumentException("") }

        // Then
        assertAll(
            { assertNotEquals(expected.id, 0L, "Persist 상태의 Entity 객체 id의 값 generated 여부 확인") },
            { assertEquals(expected.email, savedMember.email, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.password, savedMember.password, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.name, savedMember.name, "입력 파라미터 항목의 일치 여부 확인") },
            { assertEquals(expected.nickname, savedMember.nickname, "입력 파라미터 항목의 일치 여부 확인") },
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

    @Test
    @DisplayName("회원 객체 조회 실패: 존재 하지 않는 Email")
    fun findByEmail_Fail_Cause_NotExistEmail() {
        // Given
        val invalidEmail = "rcn115@naver.com"

        // When & Then
        assertThrows(NoSuchElementException::class.java) {
            memberRepo.findByEmail(invalidEmail).orElseThrow()
        }.let {
            assertTrue(it is RuntimeException)
        }
    }

    @Test
    @DisplayName("회원 권한 추가")
    fun addRoles() {
        // Given
        val savedMember = memberRepo.saveAndFlush(MemberGenerator.member())
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        savedMember.addRoles(additionRoles)
        flushAndClear()

        // Then
        val expected = memberRepo.findById(savedMember.id).orElseThrow() { throw IllegalArgumentException("") }
        assertAll(
            { assertMember(expected, savedMember) },
            { assertTrue(savedMember.roles.containsAll(additionRoles), "추가한 권한의 적용 여부 확인") }
        )
    }

    @Test
    @DisplayName("회원 권한 추가 실패: 이미 존재하는 권한")
    fun addRoles_Fail_CauseAlreadyExistRoles() {
        // Given
        val savedMember = memberRepo.saveAndFlush(MemberGenerator.member())

        // When
        val exception: RuntimeException = assertThrows(IllegalArgumentException::class.java) {
            savedMember.addRoles(savedMember.roles)
        }

        // Then
        val expected = memberRepo.findById(savedMember.id).orElseThrow() { throw IllegalArgumentException("") }
        Assertions.assertAll(
            { assertMember(expected, savedMember) },
            { assertEquals(exception.javaClass.simpleName, IllegalArgumentException::class.java.simpleName) },
            { assertEquals(exception.message, "이미 존재하는 권한 입니다.") }
        )
    }

    @Test
    @DisplayName("회원 권한 제거")
    fun removeRoles() {
        // Given
        val savedMember = memberRepo.saveAndFlush(MemberGenerator.member())
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)
        savedMember.addRoles(additionRoles)
        flush()

        val removalRoles = setOf(MemberRole.UNCERTIFIED_MEMBER, MemberRole.ADMIN)

        // When
        savedMember.removeRoles(removalRoles)
        flush()

        // Then
        val expected = memberRepo.findById(savedMember.id).orElseThrow() { throw IllegalArgumentException("") }
        assertAll(
            { assertMember(expected, savedMember) },
            { assertFalse(savedMember.roles.containsAll(removalRoles), "제거된 권한의 적용 여부 확인") }
        )
    }

    @Test
    @DisplayName("회원 권한 제거 실패: 모든 권한 제거 시 예외")
    fun exceptionByRemoveAllRoles() {
        // Given
        val savedMember = memberRepo.saveAndFlush(MemberGenerator.member())

        // When
        val exception: RuntimeException = assertThrows(IllegalArgumentException::class.java) {
            savedMember.removeRoles(savedMember.roles)
        }

        // Then
        val expected = memberRepo.findById(savedMember.id).orElseThrow() { throw IllegalArgumentException("") }
        Assertions.assertAll(
            { assertMember(expected, savedMember) },
            { assertEquals(exception.javaClass.simpleName, IllegalArgumentException::class.java.simpleName) },
            { assertEquals(exception.message, "최소 하나 이상의 권한이 존재해야 합니다.") }
        )
    }
}