package me.whypie.domain.repository

import me.whypie.domain.model.entity.project.Project
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author : choi-ys
 * @date : 2021/10/12 2:18 오후
 */
interface ProjectRepo : JpaRepository<Project, Long> {
    fun existsByName(name: String): Boolean

    @EntityGraph(attributePaths = ["member"])
    fun findAllByMemberId(id: Long, pageable: Pageable): Page<Project>
}