package me.whypie.repository

import me.whypie.model.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 10:59
 */
interface MemberRepo : JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<Member>
}