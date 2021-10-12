package me.whypie.generator

import me.whypie.model.entity.member.Member
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

    fun savedMember(): Member {
        return memberRepo.saveAndFlush(member())
    }

    fun savedMember(member: Member): Member {
        return memberRepo.saveAndFlush(member)
    }

    companion object {
        var email = "test@naver.com"
        var password = "password"
        var name = "최용석"
        var nickname = "김턱상"

        fun member() = Member(name = name, email = email, password = password, nickname = nickname)
    }
}