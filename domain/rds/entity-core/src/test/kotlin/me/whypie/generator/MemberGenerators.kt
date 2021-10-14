package me.whypie.generator

import me.whypie.model.entity.member.Member
import me.whypie.model.entity.member.MemberRole
import me.whypie.repository.MemberRepo
import org.springframework.boot.test.context.TestComponent
import org.springframework.test.context.TestConstructor

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 3:41
 */
@TestComponent
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberGenerator(
    private val memberRepo: MemberRepo,
) {

    fun savedUnCertifiedMember(): Member {
        return memberRepo.saveAndFlush(member())
    }

    fun savedCertifiedMember(): Member {
        val member = Member(name = name,
            email = email,
            password = password,
            nickname = nickname,
            roles = mutableSetOf(MemberRole.CERTIFIED_MEMBER))
        return memberRepo.saveAndFlush(member)
    }

    fun savedMember(member: Member): Member {
        return memberRepo.saveAndFlush(member)
    }

    companion object {
        var email = "project.log.062@gamil.com"
        var password = "password"
        var name = "최용석"
        var nickname = "noel"

        fun member() = Member(name = name, email = email, password = password, nickname = nickname)
    }
}