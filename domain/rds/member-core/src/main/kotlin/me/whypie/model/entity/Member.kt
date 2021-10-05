package me.whypie.model.entity

import me.whypie.model.entity.base.Auditor
import javax.persistence.*

/**
 * @author : choi-ys
 * @date : 2021/09/17 2:55 오전
 */
@Entity
@Table(
    name = "member_tb",
    uniqueConstraints = [
        UniqueConstraint(
            name = "MEMBER_EMAIL_UNIQUE",
            columnNames = ["email"]
        )
    ]
)
data class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @Column(name = "password", nullable = false, length = 100)
    var password: String,

    @Column(name = "name", nullable = false, length = 10)
    var name: String,

    @Column(name = "nickname", nullable = false, length = 10)
    var nickname: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "member_role_tb",
        joinColumns = [JoinColumn(
            name = "member_id",
            foreignKey = ForeignKey(name = "TB_MEMBER_ROLE_MEMBER_ID_FOREIGN_KEY")
        )]
    )
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<MemberRole> = mutableSetOf(MemberRole.UNCERTIFIED_MEMBER),

) : Auditor() {

    fun completeCertification() {
        roles = mutableSetOf(MemberRole.CERTIFIED_MEMBER)
    }

    fun addRoles(additionRoles: Set<MemberRole>) {
        roles.addAll(additionRoles)
    }

    fun removeRoles(removalRoles: Set<MemberRole>) {
        if (removalRoles == roles) {
            throw IllegalArgumentException("최소 하나 이상의 권한이 존재해야 합니다.")
        }
        roles.removeAll(removalRoles)
    }

}