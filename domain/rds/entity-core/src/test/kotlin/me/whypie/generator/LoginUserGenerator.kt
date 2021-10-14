package me.whypie.generator

import me.whypie.model.LoginUser
import me.whypie.model.entity.member.Member
import org.springframework.boot.test.context.TestComponent
import org.springframework.test.context.TestConstructor

/**
 * @author : choi-ys
 * @date : 2021-10-13 오전 5:51
 */
@TestComponent
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class LoginUserGenerator(
    private val memberGenerator: MemberGenerator,
) {
    fun generateLoginUser(): LoginUser {
        val savedMember = memberGenerator.savedUnCertifiedMember()
        return LoginUser(savedMember.email, savedMember.mapToSimpleGrantedAuthority())
    }

    companion object {
        fun generateLoginUserMock(member: Member): LoginUser {
            return LoginUser(member.email, member.mapToSimpleGrantedAuthority())
        }
    }
}