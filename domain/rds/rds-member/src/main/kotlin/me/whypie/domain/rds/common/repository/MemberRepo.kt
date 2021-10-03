package me.whypie.domain.rds.common.repository

import me.whypie.domain.rds.common.model.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author : choi-ys
 * @date : 2021-10-02 오후 10:59
 */
interface MemberRepo : JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean
}