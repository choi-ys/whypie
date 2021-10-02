package me.whypie.domain.generator

import me.whypie.domain.model.entity.Member
import me.whypie.domain.repository.MemberRepo
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author : choi-ys
 * @date : 2021-10-03 오전 3:41
 */

class MemberGenerator {

    @Autowired
    lateinit var memberRepo: MemberRepo

    fun savedMember(): Member {
        return memberRepo.saveAndFlush(member())
    }

    companion object {
        var email = "test@naver.com"
        var password = "password"
        var name = "최용석"
        var nickname = "김턱상"

        fun member() = Member(name = name, email = email, password = password, nickname = nickname)
    }
}